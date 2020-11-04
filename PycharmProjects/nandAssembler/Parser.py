import re
from enum import Enum

# ######################################### MAGIC_NUMBERS ############################################
S_PATTERN = re.compile('[AMD]([<>])\\1')
INIT_COUNTER = 0
A_COMMAND_START = '@'
INVALID_COMMAND = "Invalid command"
COMMAND_IDENTIFIER = 0
EQUAL_SIGN = "="
EMPTY = ""
JUMP_PREFIX = ";"
COMP_INDEX_IN_JMP = 0
JMP_INDEX = -1
COMP_INDEX_IN_EQUAL = -1
DEST_INDEX_IN_EQUAL = 0
# ####################################################################################################


class CommandTypes(Enum):
    """
    Enum to represent different command types
    """
    C = 1  # C instruction
    A = 2  # A instruction
    Shift = 3  # Shift operation


class Parser:
    """
    Parser class- manages the parsing of instructions by determining which type they are and dividing each
    instruction accordingly
    """

    def __init__(self, instructions):
        self._commands = instructions
        self._currentCommand = INIT_COUNTER

    def has_more_commands(self):
        """
        checks if there are more command to parse
        @return: true if they are more, false otherwise
        """
        return self._currentCommand < len(self._commands)

    def advance(self):
        """
        advances the command counter by 1 if there are more commands to parse
        """
        if self.has_more_commands():
            self._currentCommand += 1
            return
        return Exception(INVALID_COMMAND)

    def command_type(self):
        """
        returns the command type of the current command being parsed
        @return: command type enum value
        """
        cur = self._commands[self._currentCommand]
        if cur[COMMAND_IDENTIFIER] == A_COMMAND_START:
            return CommandTypes.A
        if re.search(S_PATTERN, cur) is not None:
            return CommandTypes.Shift
        return CommandTypes.C

    def symbol(self):
        """
        gets the symbol part of the command (if there is)
        @return: the symbol without @
        """
        cur = self._commands[self._currentCommand]
        if self.command_type() == CommandTypes.A:
            # without @
            return cur[1:]
        return Exception(INVALID_COMMAND)

    def dest(self):
        """
        gets the dest part of the command (if there is)
        @return: the dest section
        """
        cur = self._commands[self._currentCommand]
        if self.command_type() != CommandTypes.A:
            if re.compile(EQUAL_SIGN).search(cur) is not None:
                return cur.split(EQUAL_SIGN)[DEST_INDEX_IN_EQUAL]
            return EMPTY

    def comp(self):
        """
        gets the comp part of the command (if there is)
        @return: the comp section
        """
        cur = self._commands[self._currentCommand]
        if self.command_type() == CommandTypes.C:
            return (cur.split(EQUAL_SIGN)[COMP_INDEX_IN_EQUAL]).split(JUMP_PREFIX)[COMP_INDEX_IN_JMP]

    def jump(self):
        """
        gets the jump part of the command (if there is)
        @return: the jump section
        """
        cur = self._commands[self._currentCommand]
        if self.command_type() != CommandTypes.A:
            if re.compile(JUMP_PREFIX).search(cur) is not None and cur[JMP_INDEX] != JUMP_PREFIX:
                return cur.split(JUMP_PREFIX)[JMP_INDEX]
            return EMPTY

    def shift(self):
        """
        gets the shift part of the command (if there is)
        @return: the shift section
        """
        cur = self._commands[self._currentCommand]
        if self.command_type() == CommandTypes.Shift:
            return (cur.split(EQUAL_SIGN)[COMP_INDEX_IN_EQUAL]).split(JUMP_PREFIX)[COMP_INDEX_IN_JMP]

    def get_line(self):
        """
        @return: returns the current command line
        """
        return self._commands[self._currentCommand]
