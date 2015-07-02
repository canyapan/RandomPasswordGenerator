/*
 * Copyright 2015 CAN YAPAN
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.canyapan.randompasswordgenerator.cli;

import com.canyapan.randompasswordgenerator.RandomPasswordGenerator;
import com.canyapan.randompasswordgenerator.RandomPasswordGeneratorException;
import org.apache.commons.cli.*;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Options options = new Options();
        options.addOption("h", false, "prints help");
        options.addOption("def", false, "generates 8 character password with default options");
        options.addOption("p", true, "password length (default 8)");
        options.addOption("l", false, "include lower case characters");
        options.addOption("u", false, "include upper case characters");
        options.addOption("d", false, "include digits");
        options.addOption("s", false, "include symbols");
        options.addOption("lc", true, "minimum lower case character count (default 0)");
        options.addOption("uc", true, "minimum upper case character count (default 0)");
        options.addOption("dc", true, "minimum digit count (default 0)");
        options.addOption("sc", true, "minimum symbol count (default 0)");
        options.addOption("a", false, "avoid ambiguous characters");
        options.addOption("f", false, "force every character type");
        options.addOption("c", false, "continuous password generation");

        CommandLineParser parser = new DefaultParser();
        CommandLine cmd = null;
        boolean printHelp = false;
        try {
            cmd = parser.parse(options, args);
        } catch (ParseException e) {
            printHelp = true;
        }

        if (printHelp || args.length == 0 || cmd.hasOption("h")) {
            HelpFormatter formatter = new HelpFormatter();
            formatter.printHelp("java -jar RandomPasswordGenerator.jar [-p <arg>] [-l] [-u] [-s] [-d] [-dc <arg>] [-a] [-f]", options);
            return;
        }

        RandomPasswordGenerator rpg = new RandomPasswordGenerator();

        if (cmd.hasOption("def")) {
            try {
                String password = rpg.withDefault()
                        .withPasswordLength(Integer.parseInt(cmd.getOptionValue("p", "8")))
                        .generate();
                System.out.println(password);
            } catch (RandomPasswordGeneratorException e) {
                e.printStackTrace();
            }

            return;
        }

        rpg.withPasswordLength(Integer.parseInt(cmd.getOptionValue("p", "8")))
                .withLowerCaseCharacters(cmd.hasOption("l"))
                .withUpperCaseCharacters(cmd.hasOption("u"))
                .withDigits(cmd.hasOption("d"))
                .withSymbols(cmd.hasOption("s"))
                .withAvoidAmbiguousCharacters(cmd.hasOption("a"))
                .withForceEveryCharacterType(cmd.hasOption("f"));

        if (cmd.hasOption("lc")) {
            rpg.withMinLowerCaseCharacterCount(Integer.parseInt(cmd.getOptionValue("lc", "0")));
        }

        if (cmd.hasOption("uc")) {
            rpg.withMinUpperCaseCharacterCount(Integer.parseInt(cmd.getOptionValue("uc", "0")));
        }

        if (cmd.hasOption("dc")) {
            rpg.withMinDigitCount(Integer.parseInt(cmd.getOptionValue("dc", "0")));
        }

        if (cmd.hasOption("sc")) {
            rpg.withMinSpecialCharacterCount(Integer.parseInt(cmd.getOptionValue("sc", "0")));
        }

        Scanner scanner = new Scanner(System.in);

        try {
            do {
                String password = rpg.generate();
                System.out.println(password);

                if (cmd.hasOption("c")) {
                    System.out.print("Another? y/N: ");
                }
            } while(cmd.hasOption("c") && scanner.nextLine().matches("^(?i:y(?:es)?)$"));
        } catch (RandomPasswordGeneratorException e) {
            System.err.println(e.getMessage());
        }
    }
}
