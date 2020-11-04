
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
    """
    SymbolTable class- contains the table with all the symbols and their addresses in an instruction file,
    this includes built in symbols and user defined symbols i.e. labels and variables
    """
    def __init__(self):
        self._counter = NON_BUILT_IN_START
        self._symbol_table = {}
        self._initialize_table()  # initializes table with pre-defined symbols

    def _initialize_table(self):
        """
        initializes the symbol table with built in symbols
        """
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

    def add_entry(self, symbol, address):
        """
        adds the given symbol and address to the symbol table if the table doesn't already contain it
        @param symbol: the symbol to add to the table
        @param address: the address for the given symbol
        """
        if not self.contains(symbol):
            self._symbol_table[symbol] = address

    def advance_symbol_counter(self):
        """
        advances the symbol counter by 1, this is only performed of a variable is added to the table
        """
        self._counter += 1

    def contains(self, symbol):
        """
        checks if a given symbol is contained in the symbol table
        @param symbol: the symbol to look for
        @return: true if the table contains the symbol, false otherwise
        """
        return symbol in self._symbol_table

    def get_address(self, symbol):
        """
        returns the address of a given symbol
        @param symbol: the symbol to get the address of
        @return: the address
        """
        return self._symbol_table[symbol]

    def get_counter(self):
        """
        @return: returns the symbol counter
        """
        return self._counter
