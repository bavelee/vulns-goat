package io.vulns.goat;

import io.easygoat.CaseInfo;
import io.easygoat.CaseRunner;
import io.easygoat.CategoryInfo;
import io.easygoat.GoatCaseScanner;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
import java.util.Map;

/**
 * @author bavelee
 * @date 2022/02/15 22:47
 */
@SpringBootTest
public class GoatCaseScannerTest {
    @Autowired
    GoatCaseScanner goatCaseScanner;

    @Test
    void testScanAndRunAll() {
        for (Map.Entry<CategoryInfo, List<CaseInfo>> entry : GoatCaseScanner.getGoatCasesMap().entrySet()) {
//            System.out.println("====> " + entry.getKey().getName() + "  " + entry.getKey().getDesc());
            for (CaseInfo ci : entry.getValue()) {
                System.out.println(ci.getUrl());
                CaseRunner.run(ci);
            }
        }
    }
}
