# -*- coding: utf-8 -*-

import json
import glob

class Var:
    def __init__(self, var_name, var_type):
        self.var_name = var_name
        self.var_type = var_type

    def __hash__(self):
        return hash(self.var_name + self.var_type)

    def __eq__(self, other):
        return self.var_name == other.var_name and self.var_type  ==  other.var_type

    def get_name(self):
        return self.var_name

    def get_type(self):
        return self.var_type

    def isNumeric(self):
        return self.var_type in ('short', 'int', 'long', 'float', 'double')

def read_trace():
    datastore = []
    for dat in glob.glob('*.dat'):

        try:
            f = open(dat, 'r')
            datastore.append(json.load(f))
        except:
            pass

    traces = {}
    for trace_dat in datastore:
        for data in trace_dat:
            if data['className'] in traces.keys():
                class_dict = traces[data['className']]
            else:
                class_dict = {}
                traces[data['className']] = class_dict

            if data['methodName'] in class_dict.keys():
                method_dict = class_dict[data['methodName']]
            else:
                method_dict = {}
                class_dict[data['methodName']] = method_dict

            var = Var(data['varName'], data['varType'])

            if var in method_dict.keys():
                value_list = method_dict[var]
            else:
                value_list = []
                method_dict[var] = value_list
            value_list.append(detect_type(var, data['varValue']))

    return traces

def verify_type(var):
    return not var.get_type() in ('byte', 'other')

def detect_type(var, value):
    if var.get_type() == 'char':
        return value
    if var.get_type() == 'boolean':
        return value == 'true'
    if var.get_type() in ('short', 'int', 'long'):
        return int(value)
    if var.get_type() in ('float', 'double'):
        return float(value)

    return value

def detect_invariant(traces):
    invariants = {}

    for className, class_dict in traces.items():
        class_map = {}
        invariants[className] = class_map
        for methodName, method_dict in class_dict.items():
            method_list = []

            for var, value_list in method_dict.items():
                max_value = value_list[0]
                min_value = value_list[0]
                a = value_list[0]

                # Constant Value: x = a
                p1 = var.get_type() != 'other'
                # Uninitialized Value: x = uninit
                p2 = var.get_type() == 'other'
                # Small Value Set: x ∈ {a,b,c}
                p3 = var.get_type() != 'other'
                # Non-zero: x != 0
                p4 = var.isNumeric()
                value_set = set()

                for value in value_list:
                    if p1:
                        p1 = value == a
                    if p2:
                        p2 = value == 'null'
                    if p3:
                        value_set.add(value)
                        p3 = len(value_set) <= 3

                    if var.isNumeric():
                        max_value = max(max_value, value)
                        min_value = min(min_value, value)

                        if p4:
                            p4 = value != 0

                if p3:
                    p3 = len(value_set) != 1
                if p1:
                    method_list.append('Constant Value: %s = %s' % (var.get_name(), str(a)))
                if p2:
                    method_list.append('Uninitialized Value: %s = uninit' % (var.get_name()))
                if p3:
                    method_list.append('Small Value Set: %s ∈ {%s}' % (var.get_name(), ','.join(str(v) for v in value_set)))
                if p4:
                    method_list.append('Non-zero: %s != 0' % (var.get_name()))
                if var.isNumeric():
                    method_list.append('Range: %s ∈ [%s, %s]' % (var.get_name(), str(min_value), str(max_value)))

                if var.get_type() == 'int' and not p1:
                    upper = max(max_value, abs(min_value))
                    for b in (2, 3, 5, 7, 11, 13, 17, 19):
                        if b > upper:
                            break
                        p5 = True
                        p6 = True
                        a = value_list[0] % b
                        for value in value_list:
                            if not p5 and not p6:
                                break
                            remainder = value % b
                            if p5:
                                p5 = remainder  == a
                            if p6:
                                p6 = remainder != 0
                        if p5:
                            method_list.append('Modulus: %s = %d (mod %d)' % (var.get_name(), a, b))
                        if p6:
                            method_list.append('Non-modulus: %s != 0 (mod %d)' % (var.get_name(), b))

                if method_list:
                    class_map[methodName] = method_list
    return invariants

def dump_invariants(invariants):
    with open('log', 'w+') as log:
        for className, class_map in invariants.items():
            print('CLASS: %s' % className)
            log.write('CLASS: %s\n' % className)
            for methodName, method_list in class_map.items():
                print('    METHOD: %s' % methodName)
                log.write('    METHOD: %s\n' % methodName)
                for inv in method_list:
                    print('        %s' % inv)
                    log.write('        %s\n' % inv)


if __name__ == "__main__":
    traces = read_trace()
    invariants = detect_invariant(traces)
    dump_invariants(invariants)
