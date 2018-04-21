package app

import groovy.util.logging.Slf4j
/**
 * Created by chipn@eway.vn on 2/6/17.
 */

@Slf4j
class Runner {
    static {
        try {
            TimeZone.setDefault(TimeZone.getTimeZone('Asia/Bangkok'))
        } catch (Exception e) {
            e.printStackTrace()
        }
    }

    static void main(String[] args) {
        System.properties.load(new FileInputStream("conf/system.propeties"))
        def appConfigFile = new File(System.getProperty('user.dir'), 'conf/application.yml')

        def config = AppConfig.newInstance(appConfigFile)

        def server = new RestServer(config)

        server.start({ event -> })


    }
}
