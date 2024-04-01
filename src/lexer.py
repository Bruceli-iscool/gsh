# lexer lexes all tokens and feeds it to the interpreter
import re
import sys


# use re to find all tokens
# tokens
TOKENS = [
]
# lexer
class Lex:
    def __init__(self, source):
        # objects
        self.source = source
        self.position = 0

    def lex(self):
        # find tokens
        while self.position < len(self.source):
            match = None
            for token_type in TOKENS:
                token_name, pattern = token_type
                regex = re.compile(pattern)
                match = regex.match(self.source, self.position)
                if match:
                    value = match.group(0)
                    yield (token_name, value)
                    self.position = match.end()
                    break
            if not match:
                # Skip whitespace characters
                if self.source[self.position].isspace():
                    self.position += 1
                    continue
                else:
                    print(f"Camel-C: SyntaxError: Unknown Identifier: {self.source[self.position]}")
                    sys.exit()