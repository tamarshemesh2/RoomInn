
# ######################################### MAGIC_NUMBERS ############################################
SCREEN_SYMBOL = ["SCREEN", 16384]
KBD_SYMBOL = ["KBD", 24576]
SP_SYMBOL = ["SP", 0]
LCL_SYMBOL = ["LCL", 1]
ARG_SYMBOL = ["ARG", 2]
THIS_SYMBOL = ["THIS", 3]
THAT_SYMBOL = ["THAT", 4]
NAME = 0
ADDRESS = 1
BUILT_IN_REGISTER = "R"
NON_BUILT_IN_START = 16
# ####################################################################################################


class SymbolTable:
    def __init__(self):
        self._counter = NON_BUILT_IN_START
        self._symbol_table = {}
        self.__initialize_table()  # initializes table with pre-defined symbols

    def __initialize_table(self):
        # initialize pre-defined symbols
        self.add_entry(SCREEN_SYMBOL[NAME], SCREEN_SYMBOL[ADDRESS])
        self.add_entry(KBD_SYMBOL[NAME], KBD_SYMBOL[ADDRESS])
        self.add_entry(SP_SYMBOL[NAME], SP_SYMBOL[ADDRESS])
        self.add_entry(LCL_SYMBOL[NAME], LCL_SYMBOL[ADDRESS])
        self.add_entry(ARG_SYMBOL[NAME], ARG_SYMBOL[ADDRESS])
        self.add_entry(THIS_SYMBOL[NAME], THIS_SYMBOL[ADDRESS])
        self.add_entry(THAT_SYMBOL[NAME], THAT_SYMBOL[ADDRESS])

        # add 16 built in registers
        for i in range(0, 16):
            self.add_entry(BUILT_IN_REGISTER + str(i), i)
        # reinitialize counter
        self._counter = NON_BUILT_IN_START

    def add_entry(self, symbol, address):
        self._symbol_table[symbol] = address
        self._counter += 1

    def contains(self, symbol):
        return symbol in self._symbol_table

    def get_address(self, symbol):
        return self._symbol_table[symbol]

    def get_counter(self):
        return self._counter
