package name.auh.tool;

import cn.wanghaomiao.seimi.annotation.Crawler;
import cn.wanghaomiao.seimi.spring.common.CrawlerCache;
import cn.wanghaomiao.seimi.struct.Request;
import cn.wanghaomiao.seimi.struct.Response;
import lombok.extern.slf4j.Slf4j;
import org.seimicrawler.xpath.JXDocument;
import org.seimicrawler.xpath.JXNode;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

@Component
@Crawler(useUnrepeated = false, useCookie = true, httpTimeOut = 5000)
@Slf4j
public class YZCrawler extends BaseCrawler {

    private final static AtomicLong counter = new AtomicLong();

    @Scheduled(initialDelay = 10, fixedRate = 2000)
    public void webMonitor() {
        String yzUrl = "https://yz.jnu.edu.cn/63/11/c699a484113/page.htm";
        Request request = Request.build(yzUrl, "parseYZ");
        CrawlerCache.getCrawlerModel("YZCrawler").sendRequest(request);
    }

    public void parseYZ(Response response) {
        JXDocument jxDocument = response.document();
        List<JXNode> fileNameList = jxDocument.selN("//div[@class='wp_articlecontent']//span//span[@style='font-family:仿宋;font-size:18px;']/text()");

        if (CollectionUtils.isEmpty(fileNameList)) {
            return;
        }

        boolean found = false;

        for (JXNode jxNode : fileNameList) {
            if (!jxNode.isString() || !jxNode.asString().contains("医")) {
                continue;
            }

            if (!jxNode.asString().contains("一")) {
                continue;
            }

            //通知
            log.error("发现了医学的文件--{}", jxNode.asString());
            //System.exit(1);
        }

        if (!found) {
            log.info("no found {}", counter.incrementAndGet());
        }
    }

}
