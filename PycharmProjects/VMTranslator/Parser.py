from enum import Enum

# add methods that do:
# split by spaces according to command type
# regex
#


class CommandTypes(Enum):
    """
    Enum to represent different command types
    """
    C_ARITHMETIC = 1  # add, sub, neg, eq, gt. lt, and, or, not
    C_PUSH = 2  # push y
    C_POP = 3  # pop x
    C_LABEL = 4  # label
    C_GOTO = 5  # goto
    C_IF = 6  # if-goto
    C_FUNCTION = 7  # function
    C_RETURN = 8  # return
    C_CALL = 9  # call


class Parser:
    def __init__(self, input_file):
        self.input_file = input_file  # Opens the input file/stream and gets ready to parse it.
        self.current_line = 0

    def has_more_commands(self):
        """
        :return: Are there more commands in the input?
        :rtype: boolean
        """

    def advance(self):
        """
        Reads the next command from the input and makes it the current command.
        Should be called only if hasMoreCommands is true.
        Initially there is no current command.
        """

    def command_type(self):
        """
        :return: Returns the type of the current VM command.
        C_ARITHMETIC is returned for all the arithmetic commands.
        :rtype: CommandTypes
        """
        #  use regex

    def arg1(self):
        """
        :return: Returns the first arg. of the current command.
        In the case of C_ARITHMETIC, the command itself (add, sub, etc.) is returned.
        Should not be called if the current command is C_RETURN.
        :rtype: string
        """

    def arg2(self):
        """
        :return: Returns the second argument of the current command.
        Should be called only if the current command is C_PUSH, C_POP, C_FUNCTION, or C_CALL
        :rtype: int
        """