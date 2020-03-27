package com.taoyuanx.ca.shellui.timer;

import com.taoyuanx.ca.shellui.config.AppConfig;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.filefilter.DirectoryFileFilter;
import org.apache.commons.io.filefilter.SuffixFileFilter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.io.File;
import java.util.Collection;

@Component
public class DelteTempCertTask {
    Logger LOG = LoggerFactory.getLogger(DelteTempCertTask.class);
    @Autowired
    AppConfig appConfig;

    /**
     * 每天凌晨4点删除超过20分钟的临时证书文件
     */
    @Scheduled(cron = "0 0 4 * * ?")
    public void delteTempCertTask() {
        deleteTempCert(appConfig.getCertBaseDir());

    }

    private void deleteTempCert(String clientCertBasePath) {
        final Long now = System.currentTimeMillis();
        final Long deleleTimeSpan = 20 * 60 * 1000L;
        Collection<File> listFiles = FileUtils.listFiles(new File(clientCertBasePath), new SuffixFileFilter("zip"), new DirectoryFileFilter() {

            @Override
            public boolean accept(File file) {
                long create = file.lastModified();
                return deleleTimeSpan + create > now;
            }

        });
        if (null != listFiles || !listFiles.isEmpty()) {
            for (File file : listFiles) {
                try {
                    if (file.isDirectory()) {
                        FileUtils.deleteDirectory(file);
                    } else {
                        FileUtils.deleteQuietly(file);
                    }
                    LOG.info("删除[{}]成功", file);
                } catch (Exception e) {
                    LOG.error("删除[{}]失败", file);
                }
            }
        }
    }

}
