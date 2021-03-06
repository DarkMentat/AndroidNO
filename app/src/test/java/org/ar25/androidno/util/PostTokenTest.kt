package org.ar25.androidno.util

import org.junit.Assert
import org.junit.Test

class PostTokenTest{

    val text1 = """<p> Державний концерн «Укроборонпром» працює над створенням платформи UkrARPA, яка об’єднає розробників, стартапи, інвестиційні фонди та військових.&nbsp;Про таку ініціативу&nbsp;повідомили&nbsp;у прес-службі концерну.</p>
<p> «Укроборонпром» та керівництво Агенції передових оборонних дослідницьких проектів США (DARPA) напередодні&nbsp;провели зустріч, під час якої заступник директора DARPA Стівен Волкер запропонував концерну взяти участь в наукових дослідженнях, які проводить агентство.</p>
<p> Про створення платформи UkrARPA повідомив і Президент України Петро Порошенко на своїй сторінці у Facebook. За його словами, інноваційна платформа стане «аналогом американської Агенції передових оборонних дослідницьких проектів, у надрах якої був створений інтернет».</p>
<p> <img alt="" class="media-element file-wysiwyg" data-fid="91072" data-media-element="1" height="350" src="/sites/default/files/16102104.jpg" width="525"></p>
<p> Нагадаємо, у липні цього року представники українських стартапів звернулися до Петра Порошенка з ініціативою створення спеціального державного інституту, який буде опікуватися питаннями інновацій. Тоді сторони домовились щодо фінансування з держбюджету напрямків, які на сьогоднішній день вирішують питання виживання держави. Зокрема, Міністерство оборони та «Укроборонпром»&nbsp;фінансуватимуть інноваційні розробки&nbsp;в галузі оборонної промисловості.</p>
<div class="media_embed" height="315px" width="560px"> \n <iframe allowfullscreen frameborder="0" height="315px" src="https://www.youtube.com/embed/MKVsErP2gig" width="560px"></iframe>\n</div>
<p> <span class="file"><img class="file-icon" alt="Audio icon" title="audio/mpeg" src="/modules/file/icons/audio-x-generic.png">
<a href="http://files.ar25.org/sites/default/files/16-10-05-bilyy_vovk_kaganec_igor_1_0.mp3" type="audio/mpeg; length=28970869">Аудіозапис виступу Ігора Каганця 5 жовтня 2016 р.</a></span></p> \n<p> </p>
<div class="media_embed" height="315px" width="560px"> \n <iframe allowfullscreen frameborder="0" height="315px" src="https://www.youtube.com/embed/DPL03jJJ_zg" width="560px"></iframe>\n</div>
<div class="file media-element file-wysiwyg" data-fid="91059" data-media-element="1"> \n</div>
<p> Довідка з ВІкіпедії:</p>
<p> <b>Аге́нтство передови́х оборо́нних дослідни́цьких прое́ктів</b>&nbsp;(<b>DARPA</b>) (англ.&nbsp;<i>Defense Advanced Research Projects Agency</i>) &nbsp;— агентство&nbsp;Міністерства оборони США, що відповідає за розробку нових технологій для використання в&nbsp;збройних силах США.</p>
"""
    val text2 = """<p> Позаминулого літа під час ремонту ванної кімнати я тимчасово відключив від водопостачання пральну машину. А коли знову прикрутив до неї заливний шланг і ввімкнув воду, то побачив, що в місці з’єднання пластмасової труби машини і металевої накидної гайки шлангу протікає вода.</p>
<p> Перевірив гумову прокладку, знову закрутив гайку – вода далі протікає. Повторив ще раз – те ж саме.</p>
<p> Тоді вирішив, що треба сильніше закрутити накидну гайку. Для цього взяв розвідний ключ, крутанув – і зірвав пластмасову різьбу.</p>
<p> Це мене дуже засмутило, адже тепер із-за цієї дурниці доведеться самому розкручувати машину або викликати майстра. &nbsp;</p>
<p> Що робити?</p>
<p> На той час у мене уже був досвід відновлення вінчестера за допомогою Живого Слова (див. відео після 10-ї хвилини). Але ж там тонка енергетика, а тут – звичайна труба.</p>
<p> Проте не все так безнадійно. Я вже знав, що <strong>події, які відбуваються з нами, визначаються простором подій, в якому ми перебуваємо</strong>. Так, негативні події відбуваються у відповідному просторі. Але якщо піднятися на вищий енергетичний рівень – у паралельний простір вищої якості – то цих негативних подій там може й не бути.</p>
<p> <strong>Тоді я подумав, що якщо я зараз піднімуся у вищий простір подій, то там не повинно бути такої негативної події, як зірвана різьба.</strong></p>
<p> Не скажу, що я абсолютно вірив у можливість цієї події. Але точно пам’ятаю, що я її дуже прагнув, адже перспектива убити купу часу на ремонт пральної машини мене абсолютно не радувала.</p>
<p> Як піднятися у простір вищих подій? Найпростіше – за допомогою Світлової допомоги, яка входить до Універсальної форми №6 Живого&nbsp; Слова. Це надійний, багаторазово перевірений спосіб.</p>
<p> Я двічі прочитав вогняне полотно Світлова допомога, а на третьому його читанні почав закручувати гайку. Причому робив це з усвідомленням, що все відбувається по-новому, так ніби попередньої спроби з зірваною різьбою просто не було. Тобто вона була, але в якомусь іншому – паралельному – світі. &nbsp;</p>
<p> І що ви думаєте? Гайка якось на диво легко закрутилася, а коли увімкнув воду – жодного протікання вже не було.</p>
<p> <strong>Цей дивний випадок підтверджує гіпотезу, що події навколо нас визначаються простором подій, а простір подій визначається станом, в якому ми перебуваємо.</strong> Тобто зовнішні події – це прямий наслідок наших внутрішніх подій.</p>
<p> Це яскрава ілюстрація езотеричного принципу «Що всередині – те й назовні». А якщо його поєднати з Арійським вольовим імперативом «Все, що робиться з власної волі – добро» і уявленням про простори подій, то стає ще цікавіше.</p>
<p> <strong>Сенс у тому, що не в наших силах змінити Всесвіт. Зате в наших силах змінити себе і опинитися у бажаному просторі подій. </strong></p>
<p> Якщо якась справа забуксувала – піднімись у вищий простір. Там два варіанти – або поставлена мета буде легко досягнута, або з’явиться краща мета.</p>
<p> Це означає, що для пошуку виходу треба рухатися не по горизонталі, а по вертикалі: «<em>Не тривожтесь, кажучи: що будемо їсти, що пити і в що одягнемося? Про все те побиваються зовнішні. Шукайте перше Царства божого та правди його, і все те вам додасться</em>» (Матвій 6.31–33). &nbsp;</p>
<p> <img alt="" src="http://files.ar25.org/sites/default/files/styles/cover/public/isus-7.jpg" style="width: 632px; height: 436px;" title="Радіймо, арії!"><b>Радіймо, арії! &nbsp;</b></p>
<p> До зустрічі!</p>
<p> &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</p>"""


    @Test fun testParseHtmlTextToTokens(){
        val tokens1 = parseHtmlTextToTokens(text1)

        Assert.assertEquals(tokens1.size, 7)
        Assert.assertTrue(tokens1[0] is PostToken.HtmlTextToken)
        Assert.assertTrue(tokens1[1] is PostToken.ImageToken)
        Assert.assertTrue(tokens1[2] is PostToken.HtmlTextToken)
        Assert.assertTrue(tokens1[3] is PostToken.YoutubeVideoToken)
        Assert.assertTrue(tokens1[4] is PostToken.AudioLinkToken)
        Assert.assertTrue(tokens1[5] is PostToken.YoutubeVideoToken)
        Assert.assertTrue(tokens1[6] is PostToken.HtmlTextToken)

        Assert.assertEquals((tokens1[0] as PostToken.HtmlTextToken).text, "<p> Державний концерн «Укроборонпром» працює над створенням платформи UkrARPA, яка об’єднає розробників, стартапи, інвестиційні фонди та військових.&nbsp;Про таку ініціативу&nbsp;повідомили&nbsp;у прес-службі концерну.</p><p> «Укроборонпром» та керівництво Агенції передових оборонних дослідницьких проектів США (DARPA) напередодні&nbsp;провели зустріч, під час якої заступник директора DARPA Стівен Волкер запропонував концерну взяти участь в наукових дослідженнях, які проводить агентство.</p><p> Про створення платформи UkrARPA повідомив і Президент України Петро Порошенко на своїй сторінці у Facebook. За його словами, інноваційна платформа стане «аналогом американської Агенції передових оборонних дослідницьких проектів, у надрах якої був створений інтернет».</p>")
        Assert.assertEquals((tokens1[1] as PostToken.ImageToken).imageUrl, "http://www.ar25.org/sites/default/files/16102104.jpg")
        Assert.assertEquals((tokens1[3] as PostToken.YoutubeVideoToken).youtubeUrl, "https://www.youtube.com/embed/MKVsErP2gig")
        Assert.assertEquals((tokens1[4] as PostToken.AudioLinkToken).title, "Аудіозапис виступу Ігора Каганця 5 жовтня 2016 р.")
        Assert.assertEquals((tokens1[4] as PostToken.AudioLinkToken).audioUrl, "http://files.ar25.org/sites/default/files/16-10-05-bilyy_vovk_kaganec_igor_1_0.mp3")
        Assert.assertEquals((tokens1[5] as PostToken.YoutubeVideoToken).youtubeUrl, "https://www.youtube.com/embed/DPL03jJJ_zg")


        val tokens2 = parseHtmlTextToTokens(text2)

        Assert.assertEquals(tokens2.size, 3)
        Assert.assertTrue(tokens2[0] is PostToken.HtmlTextToken)
        Assert.assertTrue(tokens2[1] is PostToken.ImageToken)
        Assert.assertTrue(tokens2[2] is PostToken.HtmlTextToken)

        Assert.assertEquals((tokens2[0] as PostToken.HtmlTextToken).text, "<p> Позаминулого літа під час ремонту ванної кімнати я тимчасово відключив від водопостачання пральну машину. А коли знову прикрутив до неї заливний шланг і ввімкнув воду, то побачив, що в місці з’єднання пластмасової труби машини і металевої накидної гайки шлангу протікає вода.</p><p> Перевірив гумову прокладку, знову закрутив гайку – вода далі протікає. Повторив ще раз – те ж саме.</p><p> Тоді вирішив, що треба сильніше закрутити накидну гайку. Для цього взяв розвідний ключ, крутанув – і зірвав пластмасову різьбу.</p><p> Це мене дуже засмутило, адже тепер із-за цієї дурниці доведеться самому розкручувати машину або викликати майстра. &nbsp;</p><p> Що робити?</p><p> На той час у мене уже був досвід відновлення вінчестера за допомогою Живого Слова (див. відео після 10-ї хвилини). Але ж там тонка енергетика, а тут – звичайна труба.</p><p> Проте не все так безнадійно. Я вже знав, що <strong>події, які відбуваються з нами, визначаються простором подій, в якому ми перебуваємо</strong>. Так, негативні події відбуваються у відповідному просторі. Але якщо піднятися на вищий енергетичний рівень – у паралельний простір вищої якості – то цих негативних подій там може й не бути.</p><p> <strong>Тоді я подумав, що якщо я зараз піднімуся у вищий простір подій, то там не повинно бути такої негативної події, як зірвана різьба.</strong></p><p> Не скажу, що я абсолютно вірив у можливість цієї події. Але точно пам’ятаю, що я її дуже прагнув, адже перспектива убити купу часу на ремонт пральної машини мене абсолютно не радувала.</p><p> Як піднятися у простір вищих подій? Найпростіше – за допомогою Світлової допомоги, яка входить до Універсальної форми №6 Живого&nbsp; Слова. Це надійний, багаторазово перевірений спосіб.</p><p> Я двічі прочитав вогняне полотно Світлова допомога, а на третьому його читанні почав закручувати гайку. Причому робив це з усвідомленням, що все відбувається по-новому, так ніби попередньої спроби з зірваною різьбою просто не було. Тобто вона була, але в якомусь іншому – паралельному – світі. &nbsp;</p><p> І що ви думаєте? Гайка якось на диво легко закрутилася, а коли увімкнув воду – жодного протікання вже не було.</p><p> <strong>Цей дивний випадок підтверджує гіпотезу, що події навколо нас визначаються простором подій, а простір подій визначається станом, в якому ми перебуваємо.</strong> Тобто зовнішні події – це прямий наслідок наших внутрішніх подій.</p><p> Це яскрава ілюстрація езотеричного принципу «Що всередині – те й назовні». А якщо його поєднати з Арійським вольовим імперативом «Все, що робиться з власної волі – добро» і уявленням про простори подій, то стає ще цікавіше.</p><p> <strong>Сенс у тому, що не в наших силах змінити Всесвіт. Зате в наших силах змінити себе і опинитися у бажаному просторі подій. </strong></p><p> Якщо якась справа забуксувала – піднімись у вищий простір. Там два варіанти – або поставлена мета буде легко досягнута, або з’явиться краща мета.</p><p> Це означає, що для пошуку виходу треба рухатися не по горизонталі, а по вертикалі: «<em>Не тривожтесь, кажучи: що будемо їсти, що пити і в що одягнемося? Про все те побиваються зовнішні. Шукайте перше Царства божого та правди його, і все те вам додасться</em>» (Матвій 6.31–33). &nbsp;</p>")
        Assert.assertEquals((tokens2[1] as PostToken.ImageToken).imageUrl, "http://files.ar25.org/sites/default/files/styles/cover/public/isus-7.jpg")
        Assert.assertEquals((tokens2[1] as PostToken.ImageToken).title, "Радіймо, арії!")
        Assert.assertEquals((tokens2[2] as PostToken.HtmlTextToken).text, "<b>Радіймо, арії! &nbsp;</b><p> До зустрічі!</p>")
    }

}