package com.inventory.pos;

import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Keeps the feature packages apart so many people can work in parallel without tangling their code together.
 * Each package may import only the packages listed for it below (and every package may use {@code shell} for routes and
 * the sidebar). If this test fails, you imported something you are not allowed to. Move the shared part down into
 * {@code core} or {@code shared}, or ask the lead before changing this table.
 */
class ArchitectureTest {

    private static final Path SOURCE = Path.of("src/main/java/com/inventory/pos");
    private static final Pattern IMPORT = Pattern.compile("^import (?:static )?com\\.inventory\\.pos\\.(\\w+)\\.", Pattern.MULTILINE);

    /** package -> the other packages it may depend on */
    private static final Map<String, Set<String>> ALLOWED = Map.ofEntries(
            Map.entry("core", Set.of()),
            Map.entry("shared", Set.of("core")),
            Map.entry("shell", Set.of("shared")),
            Map.entry("demo", Set.of("core")),

            Map.entry("inventory", Set.of("core", "shared")),
            Map.entry("invoices", Set.of("core", "shared")),
            Map.entry("debt", Set.of("core", "shared", "invoices")),
            Map.entry("customers", Set.of("core", "shared", "debt")),
            Map.entry("register", Set.of("core", "shared", "inventory", "invoices", "customers")),

            // Features still to be built (see docs/WORK-PACKAGES.md)
            Map.entry("vendors", Set.of("core", "shared")),
            Map.entry("vendorcomms", Set.of("core", "shared", "vendors")),
            Map.entry("purchasing", Set.of("core", "shared", "vendors")),
            Map.entry("payables", Set.of("core", "shared", "vendors", "purchasing")),
            Map.entry("vendorreturns", Set.of("core", "shared", "vendors", "purchasing", "payables")),
            Map.entry("contracts", Set.of("core", "shared", "vendors")),
            Map.entry("customerreturns", Set.of("core", "shared", "invoices")),
            Map.entry("salesreports", Set.of("core", "shared", "invoices")),
            Map.entry("loyalty", Set.of("core", "shared", "customers")));

    @Test
    void everyPackageHasARuleHere() throws IOException {
        try (Stream<Path> folders = Files.list(SOURCE)) {
            List<String> unknown = folders.filter(Files::isDirectory)
                    .map(p -> p.getFileName().toString())
                    .filter(name -> !ALLOWED.containsKey(name))
                    .toList();
            assertThat(unknown)
                    .as("New top-level packages need a line in ALLOWED in ArchitectureTest")
                    .isEmpty();
        }
    }

    @Test
    void packagesOnlyImportWhatTheyAreAllowedTo() throws IOException {
        List<String> violations = new ArrayList<>();
        try (Stream<Path> files = Files.walk(SOURCE)) {
            for (Path file : files.filter(p -> p.toString().endsWith(".java")).toList()) {
                Path relative = SOURCE.relativize(file);
                if (relative.getNameCount() < 2) {
                    continue; // PosApplication sits in the root and may see everything
                }
                String owner = relative.getName(0).toString();
                Set<String> allowed = ALLOWED.getOrDefault(owner, Set.of());
                Matcher imports = IMPORT.matcher(Files.readString(file));
                while (imports.find()) {
                    String target = imports.group(1);
                    boolean fine = target.equals(owner) || target.equals("shell") || allowed.contains(target);
                    if (!fine) {
                        violations.add(relative.toString().replace('\\', '/') + " imports from '" + target
                                + "' but '" + owner + "' may only use " + allowed + " and shell");
                    }
                }
            }
        }
        assertThat(violations).as("Forbidden imports between packages").isEmpty();
    }
}
