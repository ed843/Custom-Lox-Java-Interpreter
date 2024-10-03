package com.ericduncandev.lox;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ModuleSystem {
    private final Map<String, Environment> modules = new HashMap<>();
    private final Interpreter interpreter;
    private boolean DEBUG = false;
    private final String coreLibraryPath;

    public ModuleSystem(Interpreter interpreter, boolean DEBUG, String coreLibraryPath) {
        this.interpreter = interpreter;
        this.DEBUG = DEBUG;
        this.coreLibraryPath = coreLibraryPath;
    }

    public void importModule(String moduleName) throws IOException {
        if (!modules.containsKey(moduleName)) {
            // Ensure the module name has the .lox extension
            if (!moduleName.endsWith(".lox")) {
                moduleName += ".lox";
            }
            if(DEBUG) {
                System.out.println("Core library path: " + coreLibraryPath);
                File coreLibDir = new File(coreLibraryPath);
                if (!coreLibDir.exists() || !coreLibDir.isDirectory()) {
                    System.err.println("Warning: Core library directory does not exist or is not a directory.");
                }
            }

            // Try to load from the current directory first
            Path currentDir = Paths.get(System.getProperty("user.dir"));
            Path filePath = currentDir.resolve(moduleName);
            if(DEBUG) {
                System.out.println("Checking current directory: " + filePath);
            }
            File file = new File(filePath.toString());
            if (!file.exists()) {
                // If not found, try to load from the core library
                filePath = Paths.get(coreLibraryPath).resolve(moduleName);
                if(DEBUG) {
                    System.out.println("Checking core library: " + filePath);
                }
                File file2 = new File(coreLibraryPath + File.separator + moduleName);
                if (!file2.exists()) {
                    throw new IOException("Module not found: " + moduleName);
                }
            }

            // Load and parse the file
            String source = new String(Files.readAllBytes(Paths.get(filePath.toUri())), Charset.defaultCharset());
            Scanner scanner = new Scanner(source);
            List<Token> tokens = scanner.scanTokens();
            Parser parser = new Parser(tokens, false);
            List<Stmt> statements = parser.parse();

            // Create a new environment for this module
            Environment moduleEnv = new Environment(interpreter.globals, DEBUG);

            Resolver resolver = new Resolver(interpreter, DEBUG);
            resolver.resolve(statements);

            // Execute the module in its own environment
            interpreter.interpret(statements);


            // Store the module's environment
            modules.put(moduleName, moduleEnv);
        }

        // Add the module's exported variables to the current environment
        Environment moduleEnv = modules.get(moduleName);
        for (Map.Entry<String, Object> entry : moduleEnv.getValues().entrySet()) {
            if (entry.getValue() instanceof LoxFunction) {
                LoxFunction function = (LoxFunction) entry.getValue();
                // Create a new function with the module's environment
                LoxFunction newFunction = new LoxFunction(
                        function.declaration,
                        function.closure,
                        function.isInitializer,
                        moduleEnv, // Pass the module's environment
                        DEBUG
                );
                interpreter.defineVariable(entry.getKey(), newFunction);
            }
            else {
                interpreter.defineVariable(entry.getKey(), entry.getValue());
            }
        }
    }

    public void exportDeclaration(String name, Object value) {
        interpreter.defineVariable(name, value);
    }
}