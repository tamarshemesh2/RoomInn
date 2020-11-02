class SymbolTable:
    def __init__(self):
        self.symbol_table = {}
        self.__initialize_table()  # initializes table with pre-defined symbols
        self.counter = 16

    def __initialize_table(self):
        # initialize pre-defined symbols
        self.symbol_table["SCREEN"] = 16384
        self.symbol_table["KBD"] = 24576
        self.symbol_table["SP"] = 0
        self.symbol_table["LCL"] = 1
        self.symbol_table["ARG"] = 2
        self.symbol_table["THIS"] = 3
        self.symbol_table["THAT"] = 4

        # add 16 built in registers
        for i in range(0, 16):
            self.symbol_table["R" + str(i)] = i

    def get_symbol_table(self):
        return self.symbol_table

    def add_variable_or_label(self, name):
        self.symbol_table[name] = self.counter
        self.counter += 1
