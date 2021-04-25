package runner;

import io.cucumber.testng.AbstractTestNGCucumberTests;
import io.cucumber.testng.CucumberOptions;
import org.testng.annotations.DataProvider;


@CucumberOptions(
        monochrome = true,
        glue = {
                "hook",
                "page",
                "stepDefinition/api",
                "runner",
                "util",
        },
        plugin = {
                "pretty", "json:htmlReport/cucumber.json"
        },
        snippets = CucumberOptions.SnippetType.CAMELCASE,
        features = {"src/test/resources/features"}
)
public class DefaultRunner extends AbstractTestNGCucumberTests{
        @Override
        @DataProvider(parallel = true)
        public Object[][] scenarios() {
                return super.scenarios();
        }}
