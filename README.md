# Lox Java Interpreter

This is a personal project where I’ve built an interpreter for the Lox programming language. Lox is a simple, dynamically-typed language created by Robert Nystrom in his book [Crafting Interpreters](https://www.craftinginterpreters.com/). This project implements the interpreter using Java, along with some additional features.

## Features
- Implements a full interpreter for the Lox language.
- Adds import and export statements
- Adds some core libraries (e.g. math_utils.lox, string_utils.lox, etc.)
- Adds array functionality in both numbers and strings
- Follows the object-oriented design patterns laid out in *Crafting Interpreters*.
- Written in pure Java.

## Setup
1. Clone the repository: `git clone`
2. Navigate to the project directory: `cd Lox-Java-Interpreter`
3. Compile the code: `mvn compile`


## Usage
- You can either start an interactive prompt: `java -cp target/classes com.ericduncandev.lox.Lox`
- or run a Lox file: `java -cp target/classes com.ericduncandev.lox.Lox [script.lox]`

## Syntax

### Classes
- **Classes** are defined using the `class` keyword.
- Methods and constructors are encapsulated within classes.

```lox
class Math {
    init() { /* constructor */ }
    
    class add(a, b) {
        return a + b;
    }
}
```

### Functions
- **Functions** are defined by using the `fun` keyword.
```lox
fun pow(base, exp) {
    var result = 1;
    for (var i = 0; i < exp; i = i + 1) {
        result = result * base;
    }
    return result;
}

```
### Methods
- Methods in classes omit the `fun` keyword
```lox
class Math {
    ceil(n) {
        return n == this.floor(n) ? n : this.floor(n) + 1;
    }
}
```
- For static methods, you use the `class` keyword before the function name
```lox
class Math {
    class add(a, b) {
            return a + b;
    }
}

print(Math.add(2, 2)) // prints 4
```

### Loops
- Loops use `for` or `while` to iterate over ranges or conditions. They use syntax similar to C.
```lox
for (var i = 0; i < 10; i = i + 1) {
    sum = sum + this.pow(-1, i) * this.pow(angle, 2 * i + 1) / this.factorial(2 * i + 1);
}
```
### Import Statements
- Use the `import` keyword to import modules.
```lox
import "math_utils"; // fine
import "math_utils.lox"; // fine too
```
### Error Handling
- Custom error messages can be displayed using the error() function.
```lox
if (b == 0) error("Division by zero.");
```

### Print Statements
- `print` outputs text to the console.
```lox
print(Math.PI);
```

### Variable Declaration
- Declare variables using the `var` keyword.
```lox
var approx = n / 2.0;
```
## Resources
- [Crafting Interpreters Book](https://www.craftinginterpreters.com/)

## License
This project is licensed under the MIT License.
