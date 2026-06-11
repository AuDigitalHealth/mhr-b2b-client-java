package au.gov.nehta.vendorlibrary.pcehr.test.unittests;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.jar.JarFile;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.junit.Test;

/**
 * Offline checks for the optional Ant wsimport path under {@code wsdls/} (EE4J tooling, not Metro).
 */
public class WsdlsCodegenToolingTest {

    private static Path repoRoot() {
        return Paths.get("").toAbsolutePath().normalize();
    }

    private static Path wsdlsDir() {
        return repoRoot().resolve("wsdls");
    }

    @Test
    public void antToolingJarsPresentWithoutMetro() throws IOException {
        Path lib = wsdlsDir().resolve("lib/provided");
        assertTrue("Missing " + lib + " — run: cd wsdls && ./sync-lib.ps1", Files.isDirectory(lib));

        List<String> jars;
        try (Stream<Path> stream = Files.list(lib)) {
            jars = stream
                    .filter(p -> p.getFileName().toString().endsWith(".jar"))
                    .map(p -> p.getFileName().toString())
                    .collect(Collectors.toList());
        }

        assertFalse("Expected tooling JARs under wsdls/lib/provided", jars.isEmpty());
        assertTrue(jars.stream().anyMatch(n -> n.startsWith("jaxws-tools-")));
        assertTrue(jars.stream().anyMatch(n -> n.startsWith("jaxws-rt-")));
        assertTrue(jars.stream().anyMatch(n -> n.contains("ant-contrib")));

        for (String name : jars) {
            assertFalse("Legacy Metro bundle must not be present: " + name, name.contains("webservices-"));
        }
    }

    @Test
    public void jaxwsToolsJarContainsWsImportAntTask() throws IOException {
        Path jar = findSingleJar(wsdlsDir().resolve("lib/provided"), "jaxws-tools-");
        try (JarFile jf = new JarFile(jar.toFile())) {
            assertNotNull(jf.getJarEntry("com/sun/tools/ws/ant/WsImport.class"));
        }
    }

    @Test
    public void wsdlTreeAndAntBuildFilesReadyForCodegen() {
        assertTrue(wsdlsDir().resolve("build.xml").toFile().isFile());
        assertTrue(wsdlsDir().resolve("build.properties").toFile().isFile());
        assertTrue(wsdlsDir().resolve("ee4j-jaxws-lib-pom.xml").toFile().isFile());
        assertTrue(wsdlsDir().resolve("src/main/java/au/gov/nehta/schema/DateAdapter.java").toFile().isFile());
        assertTrue(wsdlsDir()
                .resolve("src/main/resources/wsdl/External/B2B_PCEHRProfile.wsdl")
                .toFile()
                .isFile());
    }

    @Test
    public void ee4jJaxwsVersionAlignedBetweenRootAndWsdlsLibPom() throws IOException {
        String rootVersion = extractXmlProperty(
                new String(Files.readAllBytes(repoRoot().resolve("pom.xml")), StandardCharsets.UTF_8),
                "ee4j.jaxws.version");
        String libPom = new String(
                Files.readAllBytes(wsdlsDir().resolve("ee4j-jaxws-lib-pom.xml")), StandardCharsets.UTF_8);
        String wsdlsVersion = extractXmlProperty(libPom, "ee4j.jaxws.version");

        assertEquals(rootVersion, wsdlsVersion);
        assertTrue(libPom.contains("<artifactId>jaxws-tools</artifactId>"));
        assertFalse(libPom.contains("webservices-rt"));
    }

    private static Path findSingleJar(Path dir, String prefix) throws IOException {
        try (Stream<Path> stream = Files.list(dir)) {
            List<Path> matches = stream
                    .filter(p -> p.getFileName().toString().startsWith(prefix))
                    .filter(p -> p.getFileName().toString().endsWith(".jar"))
                    .collect(Collectors.toList());
            assertEquals("Expected exactly one " + prefix + "*.jar in " + dir, 1, matches.size());
            return matches.get(0);
        }
    }

    private static String extractXmlProperty(String xml, String name) {
        Matcher m = Pattern.compile("<" + name + ">([^<]+)</" + name + ">").matcher(xml);
        assertTrue("Property " + name + " not found", m.find());
        return m.group(1).trim();
    }
}
