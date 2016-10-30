package org.ar25.androidno.api

import okhttp3.ResponseBody
import org.ar25.androidno.BuildConfig
import org.ar25.androidno.entities.Post
import org.junit.Assert.assertEquals
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.powermock.api.mockito.PowerMockito
import org.powermock.core.classloader.annotations.PowerMockIgnore
import org.powermock.core.classloader.annotations.PrepareForTest
import org.powermock.modules.junit4.rule.PowerMockRule
import org.robolectric.RobolectricGradleTestRunner
import org.robolectric.annotation.Config


@RunWith(RobolectricGradleTestRunner::class)
@Config(constants = BuildConfig::class, sdk = intArrayOf(21))
@PowerMockIgnore("org.mockito.*", "org.robolectric.*", "android.*")
@PrepareForTest(ResponseBody::class)
class HtmlResponsePostConverterTest {

    internal var mTestHtml = "<!DOCTYPE html PUBLIC \"-//W3C//DTD XHTML+RDFa 1.0//EN\" \"http://www.w3.org/MarkUp/DTD/xhtml-rdfa-1.dtd\"><html xmlns=\"http://www.w3.org/1999/xhtml\" xml:lang=\"uk\" version=\"XHTML+RDFa 1.0\" dir=\"ltr\"><head> <meta charset=\"utf-8\"/> <base href='http://ar25.org/article/chomu-shchaslyvi-lyudy-bilsh-uspishni.html'/> <title>Чому щасливі люди більш успішні</title> <script type=\"text/javascript\" src=\"http://ar25.org/sites/default/files/js/js_yVtYIqYz42NW7tPAMixkTaennLqLTDl41KQbCoeWbUM.js\"></script><script type=\"text/javascript\" src=\"http://ar25.org/sites/default/files/js/js_N1ucDYD98m-RxWNZ_-_Y1EjKGGACTJEkhKqXFfyuCxo.js\"></script><script type=\"text/javascript\" src=\"http://ar25.org/sites/default/files/js/js_bYTn_hnm6FTYFC4MX4jXq1dgjDpTuSA-Xc_bnf6q1tM.js\"></script><script type=\"text/javascript\" src=\"http://ar25.org/sites/default/files/js/js_yCq8n852W_fCDxkT-t2Fj6SlAypQ5G3Y9bACR0h2dao.js\"></script><script type=\"text/javascript\"><![CDATA[//><!--jQuery.extend(Drupal.absolute_messages,{\"dismiss\":{\"status\":1,\"warning\":1,\"error\":0},\"dismiss_time\":{\"status\":\"3\",\"warning\":\"7\",\"error\":\"\"},\"dismiss_all_count\":\"2\"});//--><!]]></script><script type=\"text/javascript\" src=\"http://ar25.org/sites/default/files/js/js_iPIuRqfiuClSC04_xoFOZn-UaU41e3i0WTeOYI4DhDY.js\"></script><script type=\"text/javascript\" src=\"http://ar25.org/sites/default/files/js/js_BfnDEePdhNDFMyA5i9OPMHHdWgPyxBkNqy59UWgiiw8.js\"></script><script type=\"text/javascript\"></script> <meta name=\"robots\" content=noindex, nofollow/> <link rel='shortcut icon' href='http://ar25.org/sites/all/storage/default_images/theme/favicon.ico' type='image/x-icon'/> <link type=\"text/css\" rel=\"stylesheet\" href=\"//ar25.org/sites/default/files/advagg_css/css__fdoPMuHWztx97X-JsXE9ApNFfKedsChcUmcA6vVknms__5JYwBRpPdunqtmlPdezFc8GzQNUivHXMymc9xl9aXbk__JB89PieR_fl_k6Lc3nIY61cqRbr4P5Dnz3OWdRnxAW4.css\" media=\"all\"/></head><body class=\"print\"> <div class=\"print-logo\"><img alt=\"Народний Оглядач\" src=\"/sites/all/themes/ninesixtyfive/images/no2.png\" id=\"logo\" class=\"print-logo\"></div><div class=\"print-site_name\">SD.ORG.UA - щоденна газета неофіційної інформації, настроїв і громадської думки</div><p/><hr class=\"print-hr\"/><div class=\"print-content\"><div class=\"entity-extra-wrapper\" data-entity_type=\"node\" data-entity_id=\"34517\"> <article class=\"node-full\"> <div class=\"page-title\"></div><div class=\"header\"> <div class=\"cat\"> </div><div class=\"field field-name-field-link field-type-link-field field-label-inline clearfix\"><div class=\"field-label\">Посилання на оригінал:&nbsp;</div><div class=\"field-items\"><div class=\"field-item even\">Новое время</div></div></div><h1 class=\"title\">Чому щасливі люди більш успішні</h1> <div class=\"date-time\">07.09.2016 - 15:15</div></div><div class=\"full-news\"> <div class=\"teaser\"> <p> Чим більш щасливим стає ваш мозок, тим більше ви стаєте креативним, продуктивним, і відкритим до нових ідей. Щастя сприяє успішності і в бізнесі.</p><div class=\"field field-name-field-img-cover field-type-image field-label-hidden\"><div class=\"field-items\"><div class=\"field-item even\"><img src=\"http://ar25.org/sites/default/files/styles/large/public/node/2016/09/34517/16060707.jpg?itok=DuyStieC\" width=\"480\" height=\"302\" alt=\"\"/></div></div></div></div><div class=\"field field-name-body field-type-text-with-summary field-label-hidden\"><div class=\"field-items\"><div class=\"field-item even\"><p> Сучасна наука більш ефективно оцінює позитивний вплив щастя на ваші розум і тіло, тому підприємства все більше цікавляться тим, як досягти його і поліпшити свої результати.</p><p> В епоху, коли творчість та інновації необхідні для конкурентної переваги, вам потрібно місце роботи, яке заохочує ідеї і високий рівень продуктивності. </p><p> Щастя розширює ваш кругозір і робить ваше мислення гнучкішим. Позитивно налаштований мозок на 31% більш продуктивний, аніж мозок у негативному, нейтральному або напруженому стані.</p><p> Чим більш щасливим стає ваш мозок, тим більше ви стаєте креативним, продуктивним, і відкритим до нових ідей.</p><p> Дослідження, проведені американською консалтинговою компанією Gallup, як і раніше, показують, що тільки 13% працівників активно беруть участь у діяльності своєї компанії. Тільки в США це може означати втрати до $550 мільярдів на рік втрати продуктивності.</p><p> Дослідження економістів з Університету Уорвіка виявило, що щасливі співробітники з точки зору продуктивності показали результати на 20% вище контрольної групи.</p><p> Те саме дослідження показало, що нещасні робітники були на 10% менш продуктивні, ніж контрольна група.</p><p> Його результати демонструють, що люди, які є більш щасливими, зазвичай:</p><p> - Ефективніше керують своїм часом;</p><p> - Проявляють більше творчого потенціалу;</p><p> - Ефективніше розв'язють проблеми;</p><p> - Краще співпрацюють для досягнення загальних цілей;</p><p> - Виявляють більші лідерські якості.</p><p> <b>Досягнення щастя для працівників</b></p><p> Якщо компанія побудує гарну корпоративну культуру, яка заохочує щастя, вона зможе генерувати кращі проекти та інновації, які могли б з'явитися в більш жорсткій атмосфері.</p><p> Культуру і щастя пов'язують дві теорії: теорія потреб Маслоу й порівняльна теорія.</p><p> Теорія потреб Маслоу стверджує, що від того, чи задовольняються ваші потреби, залежить, чи буде щасливим ваше життя. Чим більша кількість потреб задовольняється, тим щасливішими будуть люди. Ця теорія також стверджує, що наші потреби нами керують тільки в тому разі, якщо потреби нижчого рівня задоволені.</p><p> Щоб у вас з'явилася мотивація займатися саморозвитком, потрібно задоволення таких базових потреб, як їжа, вода, безпека тощо.</p><p> Піраміда Маслоу може бути легко використана, щоб показати кореляцію між тим, які потреби мають бути задоволені на організаційному рівні, щоб працівники розвивалися.</p><p> Порівняльна теорія показує, що людське щастя залежить від порівняння між реальними стандартами якості життя і сприймаються життєвими обставинами - так званих контрольних показників.</p><p> За допомогою контрольних показників ми можемо побачити все більше прикладів організацій, які успішно задовольняють потреби своїх співробітників, що дозволяє їм рости і розвиватися.</p><p> До прикладу, нещодавно Google запровадила програму, яка називається «інноваційна перерва» - 20% робочого часу працівники можуть присвячувати потрібним проектам, які забезпечують компанію креативними та інноваційними ідеями. Так з'явилися Gmail, Google Earth і Google Talk.</p><p> Саме тому, якщо ви створите корпоративну культуру, покликану забезпечувати співробітникам вищий рівень щастя, ви отримаєте набагато вищі результати.</p></div></div></div><div class=\"field field-name-field-interests field-type-text-long field-label-above\"><div class=\"field-label\">Наші інтереси:&nbsp;</div><div class=\"field-items\"><div class=\"field-item even\"><p> Ми прийшли у це життя, щоб бути щасливими.</p></div></div></div></div><div class=\"footer clearfix\"> <div class=\"blog-autor clearfix\"> <div class=\"autor\">Гравець: <span class=\"username\">Олена Каганець</span></div></div><div class=\"files-tabs\"></div><table class=\"colorbox-table\"> <tr> <td class=\"halia-actions\"> </td><td> <div class=\"node-full-icons\"> </div><div class=\"share42init\" data-url=\"http://ar25.org/article/chomu-shchaslyvi-lyudy-bilsh-uspishni.html\" data-title=\"Чому щасливі люди більш успішні\" data-image=\"http://ar25.org/sites/default/files/node/2016/09/34517/16060707.jpg\"></div><script type=\"text/javascript\" src=\"/sites/all/libraries/share42/share42.js\"></script> </td><td class=\"statistic\"> <span>Переглядів</span> <span class=\"visits_counter\">0</span> </td></tr></table> </div><div id=\"group-halia-results\"> </div><div class=\"page-url\" id=\"page-url\"> <h3>Посилання на сторінку</h3> <div class=\"clearfix\"><label>Скорочене</label><input value=\"http://ar25.org/node/34517\"/> </div><div class=\"clearfix\"><label>Повне</label><input value=\"http://ar25.org/article/chomu-shchaslyvi-lyudy-bilsh-uspishni.html\"/></div></div><div id=\"comments\" class=\"comment-wrapper comment-wrapper-nid-34517\"> <h2 class=\"title\">Коментарі</h2> <div class=\"entity-extra-wrapper\" data-entity_type=\"comment\" data-entity_id=\"40883\"><div class=\"comment clearfix\"> <h3 class=\"title\">&quot;Новое время&quot; - бренд ?</h3> <div class=\"left\"> <div class=\"user-picture\"> <img src=\"http://ar25.org/sites/default/files/styles/profile/public/avatars/mayyaukrayinska.jpg?itok=iMicPbqy&amp;c=294b9a6f2ad4a6ada27f79d28e7ff00d\" alt=\"Зображення користувача Майя Українська.\" title=\"Зображення користувача Майя Українська.\"/> </div></div><div class=\"submitted\"> <time>Опубліковано <span class=\"username\">Майя Українська</span> 7 Вересень, 2016 - 17:34</time> </div><div class=\"content\"> <div class=\"field field-name-comment-body field-type-text-long field-label-hidden\"><div class=\"field-items\"><div class=\"field-item even\"><p>\"Новое время\" - бренд ?<br/> \"Сучасна наука більш ефективно оцінює позитивний вплив щастя на ваші розум і тіло, тому підприємства все більше цікавляться тим, як досягти його і поліпшити свої результати.\" <br/>Підприємства...<br/>Ми прийшли у це життя, щоб бути щасливими.</p></div></div></div><span class=\"authcache-p13n-asm-field-comment-field-comment-halia-voting\" data-p13n-frag=\"field\" data-p13n-param=\"40883:full:uk\"></span> <div class=\"user-signature clearfix\"> <p>Радіймо! Тільки шляхом творення сьогодення, майбутнє вітаємо чистотою, світлом перемагаємо!</p></div></div></div></div></div></article></div></div><div class=\"print-footer\"></div><hr class=\"print-hr\"/> <div class=\"print-source_url\"> <strong>Постійна адреса статті:</strong> http://ar25.org/node/34517 </div><div class=\"print-links\"></div></body></html>"

    @JvmField @Rule var rule = PowerMockRule()

    @Test fun testConvert() {
        val expectedPost = Post(
                34517L,
                "Чому щасливі люди більш успішні",
                "07.09.2016",
                "http://ar25.org/sites/default/files/styles/large/public/node/2016/09/34517/16060707.jpg?itok=DuyStieC",
                "<p> Чим більш щасливим стає ваш мозок, тим більше ви стаєте креативним, продуктивним, і відкритим до нових ідей. Щастя сприяє успішності і в бізнесі.</p>",
                "<p> Сучасна наука більш ефективно оцінює позитивний вплив щастя на ваші розум і тіло, тому підприємства все більше цікавляться тим, як досягти його і поліпшити свої результати.</p>\n" +
                        "<p> В епоху, коли творчість та інновації необхідні для конкурентної переваги, вам потрібно місце роботи, яке заохочує ідеї і високий рівень продуктивності. </p>\n" +
                        "<p> Щастя розширює ваш кругозір і робить ваше мислення гнучкішим. Позитивно налаштований мозок на 31% більш продуктивний, аніж мозок у негативному, нейтральному або напруженому стані.</p>\n" +
                        "<p> Чим більш щасливим стає ваш мозок, тим більше ви стаєте креативним, продуктивним, і відкритим до нових ідей.</p>\n" +
                        "<p> Дослідження, проведені американською консалтинговою компанією Gallup, як і раніше, показують, що тільки 13% працівників активно беруть участь у діяльності своєї компанії. Тільки в США це може означати втрати до $550 мільярдів на рік втрати продуктивності.</p>\n" +
                        "<p> Дослідження економістів з Університету Уорвіка виявило, що щасливі співробітники з точки зору продуктивності показали результати на 20% вище контрольної групи.</p>\n" +
                        "<p> Те саме дослідження показало, що нещасні робітники були на 10% менш продуктивні, ніж контрольна група.</p>\n" +
                        "<p> Його результати демонструють, що люди, які є більш щасливими, зазвичай:</p>\n" +
                        "<p> - Ефективніше керують своїм часом;</p>\n" +
                        "<p> - Проявляють більше творчого потенціалу;</p>\n" +
                        "<p> - Ефективніше розв'язють проблеми;</p>\n" +
                        "<p> - Краще співпрацюють для досягнення загальних цілей;</p>\n" +
                        "<p> - Виявляють більші лідерські якості.</p>\n" +
                        "<p> <b>Досягнення щастя для працівників</b></p>\n" +
                        "<p> Якщо компанія побудує гарну корпоративну культуру, яка заохочує щастя, вона зможе генерувати кращі проекти та інновації, які могли б з'явитися в більш жорсткій атмосфері.</p>\n" +
                        "<p> Культуру і щастя пов'язують дві теорії: теорія потреб Маслоу й порівняльна теорія.</p>\n" +
                        "<p> Теорія потреб Маслоу стверджує, що від того, чи задовольняються ваші потреби, залежить, чи буде щасливим ваше життя. Чим більша кількість потреб задовольняється, тим щасливішими будуть люди. Ця теорія також стверджує, що наші потреби нами керують тільки в тому разі, якщо потреби нижчого рівня задоволені.</p>\n" +
                        "<p> Щоб у вас з'явилася мотивація займатися саморозвитком, потрібно задоволення таких базових потреб, як їжа, вода, безпека тощо.</p>\n" +
                        "<p> Піраміда Маслоу може бути легко використана, щоб показати кореляцію між тим, які потреби мають бути задоволені на організаційному рівні, щоб працівники розвивалися.</p>\n" +
                        "<p> Порівняльна теорія показує, що людське щастя залежить від порівняння між реальними стандартами якості життя і сприймаються життєвими обставинами - так званих контрольних показників.</p>\n" +
                        "<p> За допомогою контрольних показників ми можемо побачити все більше прикладів організацій, які успішно задовольняють потреби своїх співробітників, що дозволяє їм рости і розвиватися.</p>\n" +
                        "<p> До прикладу, нещодавно Google запровадила програму, яка називається «інноваційна перерва» - 20% робочого часу працівники можуть присвячувати потрібним проектам, які забезпечують компанію креативними та інноваційними ідеями. Так з'явилися Gmail, Google Earth і Google Talk.</p>\n" +
                        "<p> Саме тому, якщо ви створите корпоративну культуру, покликану забезпечувати співробітникам вищий рівень щастя, ви отримаєте набагато вищі результати.</p>")

        val responseBody = PowerMockito.mock(ResponseBody::class.java)
        PowerMockito.`when`(responseBody.string()).thenReturn(mTestHtml)

        val converter = HtmlResponsePostConverter()

        assertEquals(expectedPost, converter.convert(responseBody))
    }
}