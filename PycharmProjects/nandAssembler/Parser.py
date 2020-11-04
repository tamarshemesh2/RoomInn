import re
from enum import Enum

# ######################################### MAGIC_NUMBERS ############################################
S_PATTERN = re.compile('[AMD]([<>]{2})')
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
    C = 1
    A = 2
    Shift = 3


class Parser:

    def __init__(self, instructions):
        self._commands = instructions
        self._currentCommand = INIT_COUNTER

    def has_more_commands(self):
        return self._currentCommand < len(self._commands)

    def advance(self):
        if self.has_more_commands():
            self._currentCommand += 1
            return
        return Exception(INVALID_COMMAND)

    def command_type(self):
        cur = self._commands[self._currentCommand]
        if cur[COMMAND_IDENTIFIER] == A_COMMAND_START:
            return CommandTypes.A
        if re.search(S_PATTERN, cur) is not None:
            return CommandTypes.Shift
        return CommandTypes.C

    def symbol(self):
        cur = self._commands[self._currentCommand]
        if self.command_type() == CommandTypes.A:
            # without @
            return cur[1:]
        return Exception(INVALID_COMMAND)

    def dest(self):
        cur = self._commands[self._currentCommand]
        if self.command_type() != CommandTypes.A:
            if re.compile(EQUAL_SIGN).search(cur) is not None:
                return cur.split(EQUAL_SIGN)[DEST_INDEX_IN_EQUAL]
            return EMPTY

    def comp(self):
        cur = self._commands[self._currentCommand]
        if self.command_type() == CommandTypes.C:
            return (cur.split(EQUAL_SIGN)[COMP_INDEX_IN_EQUAL]).split(JUMP_PREFIX)[COMP_INDEX_IN_JMP]

    def jump(self):
        cur = self._commands[self._currentCommand]
        if self.command_type() != CommandTypes.A:
            if re.compile(JUMP_PREFIX).search(cur) is not None and cur[JMP_INDEX] != JUMP_PREFIX:
                return cur.split(JUMP_PREFIX)[JMP_INDEX]
            return EMPTY

    def shift(self):
        cur = self._commands[self._currentCommand]
        if self.command_type() == CommandTypes.Shift:
            return (cur.split(EQUAL_SIGN)[COMP_INDEX_IN_EQUAL]).split(JUMP_PREFIX)[COMP_INDEX_IN_JMP]
