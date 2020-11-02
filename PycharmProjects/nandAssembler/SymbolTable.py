class SymbolTable:
    def __init__(self):
        self.counter = 16
        self.symbol_table = {}
        self.__initialize_table()  # initializes table with pre-defined symbols

    def __initialize_table(self):
        # initialize pre-defined symbols
        self.add_entry("SCREEN", 16384)
        self.add_entry("KBD", 24576)
        self.add_entry("SP", 0)
        self.add_entry("LCL", 1)
        self.add_entry("ARG", 2)
        self.add_entry("THIS", 3)
        self.add_entry("THAT", 4)

        # add 16 built in registers
        for i in range(0, 16):
            self.add_entry("R" + str(i), i)

        self.counter = 16

    def get_symbol_table(self):
        return self.symbol_table

    def add_entry(self, symbol, address):
        self.symbol_table[symbol] = address
        self.counter += 1

    def contains(self, symbol):
        return symbol in self.symbol_table

    def get_address(self, symbol):
        return self.symbol_table[symbol]

    def get_counter(self):
        return self.counter
