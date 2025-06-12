import base64

encoded = "pV5eAFjRZm9Y2Uuc27gjI3RXs7k"
# Add padding if missing
missing_padding = len(encoded) % 4
if missing_padding:
    encoded += '=' * (4 - missing_padding)

decoded = base64.b64decode(encoded)
print(decoded)
