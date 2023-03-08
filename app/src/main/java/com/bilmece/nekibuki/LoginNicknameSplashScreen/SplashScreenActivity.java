package com.bilmece.nekibuki.LoginNicknameSplashScreen;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bilmece.nekibuki.MenuActivity.Home;
import com.bilmece.nekibuki.R;

import java.util.HashMap;

public class SplashScreenActivity extends AppCompatActivity {
    //Splas Sırasında kullanılanlar
    ProgressBar progressBar;

    TextView textView;
    SQLiteDatabase database;
    float maksimumProgres = 100f, artacakProgres, progresmiktari = 0;
    Cursor cursor;
    static public HashMap<String, String> sorularHashmap;
    SQLiteStatement statement;
    String sqlSorgusu;
    MediaPlayer gameTheme;
    //Sorular için listeler
    private final String[] sorularList = {"Bu Bir Yiyecek Veya Yemek Sizce Ne ki Bu ki ?", "Bu Bir Eşya Sizce Ne ki Bu ki ?", "Bu Bir Hayvan Sizce Ne ki Bu ki ?", "Bu Türkiye'de Bulunan Bir İl Sizce Ne ki Bu ki ?", "Bu Bir Ülke Sizce Ne ki Bu ki ?", "Bu Bir Ünlü Veya Sanatçı Sizce Ne ki Bu ki ?", "Bu Bir Spor Kulubü Veya Spor Branşı Sizce Ne ki Bu ki ?"};
    private final String[] sorularKodList = {"yiyecekS1", "esyaS1", "hayvanS1", "illerS1", "ulkelerS1", "sanatciS1", "sporS1"};
    private final String[] kelimelerList = new String[]{
            "Acur", "Ada Çayı", "Ahlat", "Ahududu", "Altın Çilek", "Anadolu Kestanesi", "Ananas", "Armut", "At Kestanesi", "Avokado", "Ayva", "Badem", "Bakla", "Balkabağı", "Bamya", "Barbunya", "Bezelye", "Bilyedin",
            "Böğürtlen", "Börülce", "Brokoli", "Brüksel Lahanası", "Biber", "Cennet Elması", "Ceviz", "Çilek", "Çeri Domates", "Dağ Muşmulası", "Dağ Çileği", "Defne", "Demirhindi", "Dere Otu", "Domates", "Döngel Meyvesi",
            "Dragon Meyvesi", "Dut", "Ebegümeci", "Ejderha Meyvesi", "Elma", "Enginar", "Erik", "Fasulye", "Fesleğen", "Fındık", "Fıstık", "Frenk İnciri", "Frenk Üzümü", "Frambuaz", "Greyfurt", "Gül", "Gilaburu", "Havuç",
            "Hıyar", "Hindistan Cevizi", "Hurma", "Hünnap", "Ihlamur", "Isırgan Otu", "Ispanak", "Ispıt", "Işkın", "İğde", "İncir", "Kabak", "Kapari", "Kabuk Tarçın", "Karalahana", "Karabiber", "Karanfil", "Karnabahar",
            "Karpuz", "Kavun", "Kayısı", "Keçiboynuzu", "Kekik", "Kereviz", "Kestane", "Keten Tohumu", "Kiraz", "Kivi", "Kuşburnu", "Kuşkonmaz", "Kuzukulağı", "Lahana", "Labada", "Limon", "Mandalina", "Mango", "Mantar",
            "Marul", "Maydanoz", "Mercimek", "Mısır", "Misket Limon", "Muşmula", "Muz", "Nane", "Nar", "Naşi Armudu", "Nektarin", "Noni Meyvesi", "Nergis", "Ökse Otu", "Pancar", "Papaya", "Patates", "Patlıcan", "Pazi",
            "Pepino", "Pırasa", "Pirinç", "Pitaya", "Pomelo", "Portakal", "Puhala", "Rezene", "Roka", "Reyhan", "Sarimsak", "Semiz Otu", "Soğan", "Şalgam", "Şeftali", "Tarçın", "Taze Fasulye", "Taze Soğan", "Tere", "Turp",
            "Üzüm", "Vişne", "Yaban Mersini", "Yeni Dünya", "Yer Elması", "Yer Fıstığı", "Yulaf", "Yasemin", "Zencefil", "Zerdali", "Zeytin", "Zerdaçal",

            "Abaküs", "Abajur", "Adaptör", "Afiş", "Ağızlık", "Ahize", "Akordeon", "Akvaryum", "Albüm", "Alet Kutusu", "Allık", "Ampül", "Amortisör", "Anten", "Araba", "Askı", "Aspiratör", "Ayakkabı", "Ayakkabı Dolabı",
            "Ayakkabı Çekeceği", "Ayna", "Ataç", "Ateri", "Atlas", "Atlet", "Atkı", "Ambalaj", "Anahtarlık", "Avize", "Akü", "Bayrak", "Bağlama", "Balta", "Bank", "Bardak", "Baskül", "Baston", "Bateri", "Battaniye",
            "Bavul", "Bere", "Beşik", "Bez", "Bıçak", "Bıçkı", "Biberon", "Biblo", "Bidon", "Bileklik", "Bilezik", "Bilgisayar", "Bilye", "Bijuteri", "Bisiklet", "Blender", "Bluz", "Bohça", "Boncuk", "Bone ", "Bornoz",
            "Boya", "Bozuk Para", "Böcek İlacı", "Branda", "Bulaşık Makinesi", "Buzdolabı", "Buzluk", "Büstiyer", "Büyüteç", "Cam", "Ceket", "Cetvel", "Cezve", "Cımbız", "Cila", "Cıvata", "Cübbe", "Cüzdan", "Çekiç",
            "Çakı", "Çuval", "Çöp Torbası", "Çim Makası", "Çorap", "Çarşaf", "Çalışma Masası", "Çömlek", "Çadır", "Çakmak", "Çalı Süpürgesi", "Çamaşır", "Çamaşırlık", "Çanta", "Çapa", "Çatal",
            "Çay Bardağı", "Çay Kaşığı", "Çaydanlık", "Çekmece", "Çerçeve", "Çiçeklik", "Çivi", "Çizme", "Çorba Kaşığı", "Çöp Sepeti", "Daktilo", "Dama Tahtası", "Dantel", "Dingil", "Darbuka", "Davul", "Dergi",
            "Dikiş Makinesi", "Diş Fırçası", "Diş Macunu", "Divit", "Dizlik", "Dolap", "Dolma Kalem", "Döşek", "Dresuar", "Duş Perdesi", "Delgeç", "Duvak", "Duvar Saati", "Düdük ", "Düğme", "Düdüklü Tencere", "Dümbelek",
            "Dümen", "Dürbün", "Ecza Dolabı", "Ekmek Sepeti", "Elbise", "Elbise Askısı", "Eldiven", "Elektrik Süpürgesi", "Elektromıknatıs", "Elektromotor", "Elektronik Gitar", "Elek", "El Havlusu", "Ellik", "Emniyet Kemeri",
            "Emzik", "Eşanjör", "Eşarp", "Etek", "Faks", "Falçata", "Fanus", "Far", "Faraş", "Fayton", "Fener", "Fermuar", "Feribot", "Fes", "Fıçı", "Fırıldak", "Fırça", "Fırın", "Fıskiye", "File", "Fincan", "Fiskos",
            "Fistan", "Fişek", "Fitil", "Flama", "Flüt", "Fondoten", "Fotoğraf", "Fotoğraf Makinesi", "Fotokopi Makinesi", "Fötür", "Fren", "Fritöz", "Frizbi", "Fular", "Futbol Topu", "Gardırop", "Gaz Lambası",
            "Gazete", "Gazetelik", "Gelinlik", "Gergef", "Gırgır", "Gitar", "Giysi", "Gramofon", "Gömlek", "Gözlük", "Güveç ", "Hırka", "Halat", "Halı", "Halhal", "Harddisk", "Hamak", "Harita", "Hasır", "Hilti",
            "Havan", "Havlu", "Hurç", "Islak Mendil", "Işıldak", "Istaka", "Izgara", "Isıtma Kabı", "İğne", "İletki", "İnci", "İp", "İpek", "İplik", "İskambil Kağıdı", "İskemle", "İşitme Cihazı",
            "Jakuzi", "Jaluzi", "Jartiyer", "Jeneratör", "Jet", "Jeton", "Jikle", "Jilet", "Jöle", "Jüpon", "Kadeh", "Kağıt", "Kalem", "Kalemtıraş", "Kalem Kutusu", "Kalorifer", "Kalorimetre", "Kanepe", "Kamış", "Kaşık",
            "Kapı", "Karbüratör", "Karton", "Karyola", "Kasa", "Kazak", "Kazma", "Kep", "Kepçe", "Kerpeten", "Kese", "Keski", "Kevgir", "Kırbaç", "Kırlent", "Kilit", "Kilim", "Kilot", "Kiremit", "Kitap", "Kitaplık",
            "Klavye ", "Klima", "Kravat", "Korniş", "Konsol", "Kolonya", "Konteyner", "Koltuk", "Kolye", "Korkuluk", "Kova", "Kukuleta", "Kulaklık", "Kumanda", "Kurdele", "Kurutma Makinesi", "Kuş Kafesi", "Kutu", "Küllük",
            "Külah", "Kül Tablası", "Küpe", "Kürdan", "Kürek", "Kütüphane", "Küvet", "Levha", "Lamba", "Laptop", "Lavabo", "Lazımlık", "Levye", "Leğen", "Lastik", "Limonluk", "Limon Sıkacağı", "Lif", "Leopar", "Makas",
            "Mendil", "Maket", "Mercek", "Makine", "Mandal", "Mandolin", "Mangal", "Manto", "Masa", "Maskara", "Maske", "Maskot", "Matkap", "Mayo", "Merdane", "Merdiven", "Merhem", "Meşale", "Muşamba", "Mızrak", "Mıknatıs",
            "Miğfer", "Mikrodalga Fırın", "Mikrofon", "Minder", "Monitör", "Motor", "Motorsiklet", "Mum", "Mumluk ", "Mücevher", "Mutfak Robotu", "Mühür", "Mürekkep", "Müzik Kutusu", "Nakış Makinesi", "Nal", "Namaz Örtüsü",
            "Namazlık", "Narenciye Sıkacağı", "Navigatör", "Olta", "Orak", "Otomobil", "Oturak", "Oyuncak", "Örs", "Örtü", "Pırlanta", "Pabuç", "Palaska", "Paça", "Palto", "Paket", "Pantolon", "Pamuk", "Para", "Pasaport",
            "Pastil", "Pastel Boya", "Paten", "Patik", "Peçete", "Peçetelik", "Penye", "Pencere", "Pense", "Perde", "Pergel", "Pervane", "Pijama", "Pike", "Pil", "Pirsing", "Pipet", "Piston", "Piyano", "Pudra", "Pul",
            "Pulluk", "Puzzle", "Radyo", "Raf", "Rulo", "Ranza", "Rozet", "Reçellik", "Reçete", "Rende", "Rimel", "Roket", "Rozet", "Ruj", "Rüzgar Gülü", "Saat", "Baykuş", "Sıra", "Sabahlık ", "Sabun", "Sabunluk",
            "Sacayağı",  "Saksafon", "Saksı", "Sal", "Salıncak", "Sargı Bezi", "Sandal", "Sandalet", "Sandalye", "Sapan", "Sap", "Satır", "Satranç", "Saz", "Seccade", "Silgi", "Sebzelik", "Silindir",
            "Sedye", "Sibop", "Sehpa", "Sepet", "Servant", "Sini", "Süpürge", "Sürahi", "Sopa", "Sütlük", "Süzgeç", "Süzgü", "Şalvar", "Şamdan", "Şampuan", "Şapka", "Şırınga", "Şaraplık", "Şarj Aleti", "Şekerlik", "Şemsiye",
            "Şartel", "Şezlong", "Şifoniyer", "Şişe", "Şnorkel", "Şofben", "Şort", "Şömine", "Tasma", "Tabak", "Tabanca", "Tabela", "Tablo", "Tabure", "Tabut", "Yat", "Yatak", "Yatak Örtüsü", "Yazma", "Yara Bandı", "Yelek",
            "Yelken", "Yelpaze", "Yorgan", "Yular", "Yumurtalık ", "Yün", "Yemek Bezi", "Yüzük", "Tespih", "Testi", "Termos", "Tıpa", "Top", "Topaç", "Torba", "Torpil", "Tost Makinesi", "Toka", "Tornavida", "Traktör", "Tren",
            "Tulum", "Tulumba", "Tuval", "Tuvalet Kağıdı", "Tuzluk", "Tüfak", "Tül", "Tülbent", "Tüp", "Tütsü", "Uhu", "Uydu", "Ufo", "Uçak", "Uçurtma", "Usturlap", "Urgan", "Ustura", "Uyku Tulumu", "Ütü", "Üzengi", "Üniforma",
            "Ütü masası", "Vakum", "Örümcek", "Vazo", "Valf", "Vana", "Vantilatör", "Vapur", "Varil", "Vargel", "Vernik", "Vestiyer", "Vida", "Vitrin", "Vinç", "Yağdanlık", "Yağlı Boya", "Yağmurluk", "Yastık", "Zar", "Zarf",
            "Zeytinlik", "Zımba", "Zımpara", "Zift", "Zil", "Zıpkın", "Zincir", "Zırh", "Zigon ", "Zikzak Makinesi", "Zurna", "Zümrüt",

            "Ayı", "Amip", "Akbaba", "Aslan", "Angus", "Aygır", "Arı", "Akrep", "Ahtapot", "Anakonda", "At ", "Atmaca", "Ateş Böceği", "Ada Tavşanı", "Afgan Tazısı", "Ağaç Arısı", "Ağaçkakan", "Ağaç Kırlangıcı", "Ağustos Böceği",
            "Ak Balina", "Akgerdan", "Alabalık", "Alaca Baykuş", "Ala Geyik", "Alakarga", "Alligator", "Antilop", "Babun", "Bakteri", "Balaban", "Balık", "Balina", "Bengal Kaplanı", "Baykuş", "Bıldırcın", "Bit", "Bizon",
            "Bok Böceği", "Koyun", "Boz Ayı", "Boğa", "Böcek", "Buffalo", "Bukalemun", "Ceylan", "Cennet Balığı", "Cırcır Böceği", "Cengel Kedisi", "Civciv", "Cennet Kuşu", "Camış", "Çakal", "Çalıkuşu", "Çavuş Kuşu",
            "Çaylak Kuşu", "Çekirge", "Çita", "Çiyan", "Çuuluk", "Dağ Keçisi", "Danaburnu", "Denizanası", "Deniz Aslanı", "Denizatı", "Deniz Kaplumbağası", "Deniz Kestanesi", "Deniz yıldızı", "Deve", "Deve Kuşu", "Dinozor",
            "Doğan", "Dokumacı Kuşu", "Domuz", "Dağ Keçisi", "Eşek", "Engerek Yılanı", "Eşek Arısı", "Ejderha", "Fare ", "Fil", "Flamingo", "Fok", "Gagalı Balina", "Gelincik", "Gelincik Balığı", "Gergedan", "Geyik", "Goril",
            "Güvercin", "Güve", "Güvercin", "Halkalı Solucan", "Hamam Böceği", "Hamsi", "Hamster", "Hindi", "Horoz", "Havana Kedisi", "Istakoz", "İbibik Kuşu", "İguana", "İmpala", "İnci Balığı", "İnek", "İpek Böceği", "İstiridye", "İstavrit",
            "Jaguar", "Jibon", "Jakana", "Japon Atmacası", "Japon Kedisi", "Japon Bıldırcını", "Japon Köpeği", "Kanarya", "Kanguru", "Kaplan", "Kaplumbağa", "Kadife Balığı", "Karınca", "Karıncayiyen", "Karga", "Kartal",
            "Katır", "Kaya Balığı", "Kaz", "Keçi", "Kedi", "Kedi Balığı", "Kefal", "Keklik", "Kelaynak", "Kelebek", "Kene", "Kertenkele", "Kar Leoparı", "Karakulak", "Kızılgerdan", "Kırkayak", "Kırlangıç", "Kirpi", "Koala",
            "Kobay", "Kokarca", "Koyun", "Köpek", "Köstebek ", "Kuğu", "Kumru", "Kunduz", "Kurbağa", "Kurt", "Kuş", "Kutup Ayısı", "Kuzgun", "Kuzu", "Küela", "Lama", "Lemming", "Lemur", "Leopar", "Levrek", "Leylek", "Lori",
            "Leş Kargası", "Makak", "Maki", "Mamut", "Manda", "Marmoset", "Mangust", "Mayıs Böceği", "Maymun", "Midilli", "Midye", "Mirket", "Muhabbet Kuşu", "Mezgit", "Martı", "Nandu", "Narval", "Notilus", "Neon Balığı",
            "Nudibranch", "Nar Bülbülü", "Maymun", "Numbat", "Nil Timsahı", "Nefila", "Nasua", "Okapi", "Orangutan", "Orkinos", "Ornitorenk", "Oselo", "Ördek", "Örümcek", "Otari", "Öküz", "Örümcek Maymunu", "Palamut",
            "Papağan", "Pars", "Panda", "Pekari", "Pelikan", "Penguen", "Patka", "Patak Maymunu", "Papuçgagalı", "Pars Kedisi", "Papaz Balığı", "Pakarana", "Peygamber Devesi", "Pirana ", "Pire", "Piton", "Plankton", "Porsuk",
            "Puma", "Rakun", "Ren Geyiği", "Ringa Balığı", "Ren Öküzü", "Ren Devesi", "Saka", "Salyangoz", "Samur", "Sansar", "Sardalya", "Sarig", "Sazan Balığı", "Semender", "Serçe", "Sıçan", "Sığır", "Sığırcık Kuşu", "Sırtlan",
            "Sincap", "Sinek", "Sivrisinek", "Shar Pei", "Saz Tuygunu", "Sakarca", "Siyam Kedisi", "Solucan", "Som Balığı", "Su Aygırı", "Sülük", "Sümüklü Böcek", "Sünger", "Şahin", "Şempanze", "Şakrak Kuşu", "Tahta Kurusu",
            "Tarantula", "Tavşan", "Pelikan", "Tavuk", "Tavuskuşu", "Tazmanya Canavarı", "Tırtıl", "Tilki", "Timsah", "Turako", "Tahta Balığı", "Tembel Hayvan", "Tirsi Balığı", "Tırtak", "Tatlı Su Levreği", "Trakya Levreği",
            "Uğur Böceği", "Uskumru", "Üveyik", "Yarasa", "Yaban Domuzu", "Yayın Balığı", "Yılan", "Yılanboyun Kuşu", "Yılan Balığı", "Yengeç", "Yunus", "Yaban Kedisi ", "Yemen Kekliği", "Yeşil Tavuskuşu", "Yoz Atmaca",
            "Yuvarlak Sardalya", "Yağmur Bıldırcını", "Zargana", "Zebra", "Zebu", "Zürafa", "Zurna Balığı",


            "İstanbul", "Ankara", "İzmir", "Adana", "Adıyaman", "Afyonkarahisar", "Ağrı", "Aksaray", "Amasya", "Antalya", "Ardahan", "Artvin", "Aydın", "Balıkesir", "Bartın", "Batman", "Bayburt", "Bilecik",
            "Bingöl", "Bitlis", "Bolu", "Burdur", "Bursa", "Çanakkale", "Çankırı", "Çorum", "Denizli", "Diyarbakır", "Düzce", "Edirne", "Elazığ", "Erzincan", "Erzurum", "Eskişehir", "Gaziantep", "Giresun",
            "Gümüşhane", "Hakkari", "Hatay", "Iğdır", "Isparta", "Kahramanmaraş", "Karabük", "Karaman", "Kars", "Kastamonu", "Kayseri", "Kırıkkale", "Kırklareli", "Kırşehir", "Kilis", "Kocaeli", "Konya",
            "Kütahya", "Malatya", "Manisa", "Mardin", "Mersin", "Muğla", "Muş", "Nevşehir", "Niğde", "Ordu", "Osmaniye", "Rize", "Sakarya", "Samsun", "Siirt", "Sinop", "Sivas", "Şırnak", "Tekirdağ", "Tokat",
            "Trabzon", "Tunceli", "Şanlıurfa", "Uşak", "Van", "Yalova", "Yozgat", "Zonguldak",

            "Abhazya", "Afganistan", "Almanya", "Abd", "Andorra", "Angola", "Antigua Barbuda", "Arjantin", "Arnavutluk", "Avustralya", "Avusturya", "Azerbaycan", "Bahamalar", "Bahreyn", "Bangladeş",
            "Barbados", "Belçika", "Belize", "Benin", "Bhutan", "Bolivya", "Botsvana", "Bae", "Brezilya", "Brunei", "Bulgaristan", "Burundi", "Çek Cumhuriyeti", "Cezayir", "Cibuti", "Çad", "Çin",
            "Danimarka", "Dominika", "Dağlık Karabağ", "Doğu Timor", "Dominik Cumhuriyeti", "Ekvador", "Endonezya", "Eritre", "Ekvator Ginesi", "El Salvador", "Fildişi Sahilleri", "Güney Afrika",
            "Ermenistan", "Estonya", "Etiyopya", "Fas", "Fiji", "Filipinler", "Filistin", "Finlandiya", "Fransa", "Gabon", "Gambiya", "Gana", "Gine", "Grenada", "Guyana", "Guatemala", "Gürcistan", "Haiti", "Hırvatistan",
            "Hindistan", "Hollanda", "Honduras", "Irak", "İngiltere", "İran", "İrlanda", "İspanya", "İsrail", "İsveç", "İsviçre", "İtalya", "İzlanda", "Jamaika", "Japonya", "Kamboçya", "Kamerun", "Kanada", "Karadağ",
            "Katar", "Kazakistan", "Kenya", "Kırgızistan", "Kiribati", "Kolombiya", "Komorlar", "Kongo", "Kosova", "Kosta Rika", "Kuveyt", "Kuzey Kore", "Küba", "Laos", "Lesotho", "Letonya", "Liberya", "Libya",
            "Liechtenstein", "Litvanya", "Lübnan", "Lüksemburg", "Macaristan", "Madagaskar", "Makedonya", "Malavi", "Maldivler", "Malezya", "Mali", "Malta", "Narshall Adaları", "Meksika", "Mısır", "Mikronezya",
            "Moğolistan", "Moldova", "Monako", "Moritanya", "Moritius", "Mozambik", "Myanmar", "Namibya", "Nauru", "Nepal", "Nikaragua", "Nijer", "Nijerya", "Norveç", "Özbekistan", "Pakistan", "Palau", "Panama", "Papua",
            "Paraguay", "Peru", "Polonya", "Portekiz", "Porto Riko", "Romanya", "Ruanda", "Rusya", "Samoa", "San Marino", "Sealand", "Senegal", "Seyşeller", "Sırbistan", "Sierra Leone", "Singapur", "Slovakya", "Slovenya",
            "Somali", "Somaliland", "Sudan", "Surinam", "Suriye", "Suudi Arabistan", "Svaziland", "Şili", "Tacikistan", "Tanzanya", "Tayland", "Tayvan", "Togo", "Tonga", "Transdinyester", "Tunus", "Tuvalu", "Türkiye",
            "Türkmenistan", "Uganda", "Ukrayna", "Umman", "Uruguay", "Ürdün", "Vanuatu", "Vatikan", "Venezuela", "Vietnam", "Yemen", "Yunanistan", "Zambiyav", "Zimbabve", "Güney Kore", "Güney Osetya", "Güney Sudan",
            "Kongo", "Makedonya Cumhuriyeti", "Marshall Adaları", "Suudi Arabistan",

            "Asena", "Ahmet Kural", "Ayşen Gruda", "Ata Demirer", "Aslı Güngör", "Ali Sunal", "Armağan Çağlayan", "Azer Bülbül", "Aysun Kayacı",  "Azra Akın", "Alişan", "Arda Türkmen", "Aslı Tandoğan",
            "Ali Kuşçu", "Ayça Bingöl", "Aydın Doğan", "Ali Kırca", "Aleyna Tilki", "Ali Atay", "Alp Kırşan", "Aslı Enver", "Ajda Pekkan", "Arda Kural", "Ali Taran", "Afra Saraçoğlu", "Ayşe Özyılmazel",
            "Asuman Krause", "Acun Ilıcalı", "Azra Akın", "Aybüke Pusat", "Alina Boz", "Ahu Tuğba",  "Burak Kut", "Bülent Ersoy", "Burcu Güneş", "Bülent Serttaş", "Buse Akar", "Burcu Biricik", "Beyazıt Öztürk",
            "Beren Saat", "Barış Arduç", "Burçin Terzioğlu", "Bekir Develi", "Buse Terim", "Bensu Soral", "Belgin Doruk", "Büşra Pekin", "Burak Özçivit", "Bülent İnal", "Bergüzar Korel", "Barış Aytaç",
            "Burcu Esmersoy", "Barış Falay", "Behzat Uygur", "Bennu Yıldırımlar", "Burak Tozkoparan", "Bade İşcil", "Berk Hakman", "Buğra Gülsoy", "Barış Akarsu", "Banu Alkan", "Barış Manço", "Bayhan", "Bedirhan Gökçe", "Bengü",
            "Bedük", "Cahit Arf", "Can Dündar", "Candan Erçetin", "Cem Adrian", "Cem Karaca", "Cem Yılmaz", "Can Yılmaz", "Cemal Süreya", "Ceyhun Yılmaz", "Ceza", "Cüneydi Bağdadi", "Cüneyt Özdemir", "Can Bonomo", "Can Yücel",
            "Cüneyt Arkın", "Cansu Dere", "Ceylan", "Cem Uçan", "Canan Karatay", "Cengiz Kurtoğlu", "Çağla Şikel", "Çağatay Ulusoy", "Çağla Kubat", "Çetin Tekindor", "Çağatay Akman", "Çağlar Ertuğrul", "Çiğdem Çelik",
            "Çağlar Çorumlu", "Çağla Demir", "Çelik", "Duygu Asena", "Davut Güloğlu", "Defne Samyeli", "Demet Akalın", "Demet Sağıroğlu", "Deniz Seki", "Dilberay", "Doğuş", "Dağhan Külegeç", "Duygu Özaslan",
            "Dilara Gönder",  "Deniz Baysal", "Doğan Cüceloğlu", "Demet Akbağ", "Demet Evgar", "Derya Büyükuncu", "Derya Uluğ", "Doğa Rutkay", "Deniz Çakır", "Demet Özdemir",
            "Doğukan Manço", "Derya Tuna", "Ebru Gündeş", "Ebru Polat", "Ebru Yaşar", "Edip Akbayram", "Ecem Onaran", "Eren Vurdem", "Elçin Sangu", "Esat Yontunç", "Ediz Hun", "Emre Karayel", "Esra Bilgiç", "Engin Akyürek",
            "Ebru Destan", "Eda Taşpınar", "Eser Yenenler", "Erkan Petekkaya", "Ece Erken", "Edis", "Elif Turan", "Emel Sayın", "Emre Altuğ", "Erman", "Erol Evgin", "Ersay Üner", "Esin Engin", "Eşref Ziya",
            "Ebu Hanife", "Ebu Hureyre", "Ece Ayhan", "Ece Temelkuran", "Edip Cansever", "Eylem", "Emre Durak", "Elif Şafak", "Emel Müftüoğlu", "Eminem", "Emrah", "Emre Aydın", "Ercan Taner", "Erdal Demirkıran",
            "Erkin Koray", "Erol Büyükburç", "Fahreddin Razi", "Fatih Altaylı", "Fazıl Say", "Ferdi Tayfur", "Filiz Akın", "Fırat Tanış", "Filiz Aker",
            "Fatih Dönmez", "Fulya Zenginer", "Fikret Hakan", "Ferhan Şensoy", "Furkan Kızılay", "Furkan Andıç", "Fatma Girik", "Ferhat Göçer", "Fahriye Evcen", "Funda Arar",
            "Fatih Ürek", "Fatih Portakal", "Fatih Kısaparmak", "Feridun Düzağaç", "Fikret Kızılok", "Füsun Önal", "Ferman Akgül", "Fettah Can", "Galip Erdem", "Gülse Birsel", "Gülşen", "Gülten Akın", "Gündüz Kılıç", "Gündüz Vassaf",
            "Gökhan Alkan", "Görkem Sevindik", "Gizem Karaca", "Gökçe Bahadır", "Gamze Aksu", "Gizem Kurtulan", "Gonca Sarıyıldız", "Gökhan Özoğuz", "Güneş Tan", "Güliz Ayla", "Gupse Özay", "Gamze Topuz", "Gülşen Bubikoğlu",
            "Gülben Ergen", "Gökhan Keser", "Gülçin Ergül", "Gürkan Uygun", "Gökhan Tepe", "Gökhan Türkmen", "Gülden Mutlu", "Gamze Özçelik", "Güvenç Selekman", "Gökçe", "Gökhan Kırdar", "Gökhan Özen", "Göksel", "Gönül Yazar",
            "Hadise Açıkgöz", "Hakan Günday", "Haldun Taner", "Halikarnas Balıkçısı", "Halil Cibran",  "Hande Altaylı", "Hayko Cepkin", "Hidayet Türkoğlu", "Hilal Cebeci",
            "Hulusi Kentmen", "Hülya Avşar", "Hüsamettin Cindoruk", "Hüseyin Çelik",  "Hande Ataizi", "Hakan Altun", "Hakan Peker", "Hande Yener", "Hakkı Bulut", "Haluk Levent", "Harun Kolçak",
            "Halil Sezai", "Hakan Hatipoğlu", "Hülya Koçyiğit", "Hilmi Cem İntepe", "Hazar Ergüçlü", "Haluk Bilginer", "Hande Soral", "Halit Ergenç", "Haldun Dormen", "Halit Akçatepe", "Hande Subaşı", "Hazal Kaya",
            "Hamdi Alkan", "Irmak Atuk", "Işın Karaca", "Neşet Ertaş", "Nevzat Çelik", "Nevzat Tarhan", "Nazan Bekiroğlu", "Nazan Öncel", "Necmettin Erbakan", "Nejat İşler", "Nejat Uygur",
            "İlber Ortaylı", "İlhami Çiçek", "İlhan Arsel", "İlker Başbuğ", "İnci Aral", "İskender Pala", "İzzet Yıldızhan",
            "İbrahim Saraçoğlu", "İsmail Düvenci", "İbrahim Yattara", "İrem Sak", "İlker İnanoğlu", "İkbal Gürpınar", "İclal Aydın", "İrem Derici", "İrem Helvacıoğlu", "İlker Ayrık", "İvana Sert", "İlayda Ildır",
            "İsmail Hacıoğlu", "İdo Tatlıses", "İpek Bilgin", "İlayda Akdoğa", "İlhan Mansız", "Yıldız Kenter", "Yılmaz Erdoğan", "Yılmaz Güney", "Yılmaz Özdil", "Yogi Bhajan", "Yunus Emre", "Yuri Gagarin", "Yusuf Atılgan",
             "Yusuf İslam", "Yağız Erdoğan", "Yusuf Karakaya", "Yunjin Kim", "Yasemin Ergene", "Yağmur Atacan", "Yıldız Tilbe", "Yunus Günçe", "Yılmaz Morgül",
            "Yasemin Yalçın", "Yiğit Bulut", "Yusuf Çim", "Yalçın Menteş", "Yasemin Allen", "Yonca Cevher", "Yonca Evcimik", "Yeliz Akkaya", "Yıldız Usmonova", "Yeşim Salkım", "Yavuz Bingöl",
            "Zekai Özger", "Zeki Alasya", "Zeyno Eracar", "Zerrin Özer", "Zeynep Koçak", "Zehra Yılmaz", "Zeynep Vuran", "Zeynep Eronat", "Zeki Demirkubuz",  "Zeki Müren", "Zeki Sezer",
            "Zelda Fitzgerald", "Zeynep Çamcı", "Ziya Paşa", "Zafer Ergin", "Zafer Akıncı", "Zuhal Olcay", "Zafer Algöz", "Zafer Mete", "Zara", "Zeynep Kankonde", "Zeynep Alkan",
            "Zahide Yetiş", "Zuhal Topal", "Zeyno Günenç", "Zerrin Tekindor", "Ziynet Sali", "Zeyd Gümüştutan", "Zeki Ökten", "Zeliha Berksoy", "Zeynep Korkmaz", "İlyas Yalçıntaş", "İbrahim Büyükak", "İzel Çeliköz",
            "İlker Aksum", "İbrahim Tatlıses", "İlker Kaleli", "İsmail Yk", "Nagehan Alçı", "Nalan Güven", "Namık Kemal", "Nasiruddin Tusi", "Naşide Gökbudak",
            "Numan Kurtulmuş", "Nuray Mert", "Nurettin Topçu", "Nergis Kumbasar", "Nevra Serezli", "Nebahat Çehre", "Nükhet Duru", "Nihat Doğan", "Nurgül Yeşilçay", "Nilüfer", "Nilay Deniz", "Nihat Altınkaya", "Neslihan Atagül", "Nusret Gökçe", "Nil Özalp", "Nurettin Sönmez", "Nursel Köse", "Nur Fettahoğlu",
            "Nil Karaibrahimgil", "Nihat Hatipoğlu", "Neslişah Alkoçlar", "Necati Şaşmaz", "Obama", "Og Mandino", "Ogün Altıparmak", "Oğuz Atay", "Okan Bayülgen", "Oktay Rifat", "Oktay Sinanoğlu", "Orçun Masatçı", "Orhan Kemal",

            "Beşiktaş", "Alanyaspor", "Altay", "Antalyaspor", "Fenerbahçe", "Çaykur Rizespor", "Fatih Karagümrük", "Fenerbahçe", "Galatasaray", "Gaziantep", "Giresunspor", "Göztepe", "Hatayspor", "İstanbul Başakşehir", "Kasımpaşa",
            "Kayserispor", "Konyaspor", "Sivasspor", "Trabzonspor", "Yeni Malatyaspor", "Lazio", "Udinese", "Milan", "Fiorentina", "Genoa", "Napoli", "Roma", "Atalanta", "Inter", "Juventus", "Athletic Bilbao", "Villarreal", "Valencia",
            "Celta Vigo", "Espanyol", "Getafe", "Granada", "Levante", "Atletico Madrid", "Barcelona", "Real Madrid", "Sevilla", "Arsenal", "Aston Villa", "Leicester City", "West Ham United", "Liverpool", "Everton", "Newcastle United",
            "Norwich City", "Southampton", "Tottenham Hotspur", "Chelsea", "Manchester City", "Manchester United", "Bordeaux", "Lille", "Monaco", "Nice", "Paris Saint Germain", "Stade Rennais", "Saint-Étienne", "Anadolu Efes",
            "Kızılyıldız", "Atletizm", "Avcılık Ve Atıcılık", "Badminton", "Basketbol", "Bilardo", "Binicilik", "Bisiklet", "Bowling", "Boks", "Biriç", "Buz Hokeyi", "Buz Pateni", "Cimnastik", "Dağcılık", "Eskrim", "Futbol", "Golf",
            "Güreş", "Halk Oyunları", "Hokey", "İzcilik", "Judo", "Kano", "Karate", "Kayak", "Kick Boks", "Kürek", "Masa Tenisi", "Muay Thai", "Okçuluk", "Ragbi", "Santraç", "Su Topu", "Taekwondo", "Tenis", "Triatlon", "Voleybol", "Wushu",
            "Yelken", "Yüzme"
    };

    private final String[] kelimelerKodList = {
            "yiyecekS1", "yiyecekS1", "yiyecekS1", "yiyecekS1", "yiyecekS1", "yiyecekS1", "yiyecekS1", "yiyecekS1", "yiyecekS1", "yiyecekS1", "yiyecekS1", "yiyecekS1", "yiyecekS1", "yiyecekS1", "yiyecekS1",
            "yiyecekS1", "yiyecekS1", "yiyecekS1", "yiyecekS1", "yiyecekS1", "yiyecekS1", "yiyecekS1", "yiyecekS1", "yiyecekS1", "yiyecekS1", "yiyecekS1", "yiyecekS1", "yiyecekS1", "yiyecekS1", "yiyecekS1",
            "yiyecekS1", "yiyecekS1", "yiyecekS1", "yiyecekS1", "yiyecekS1", "yiyecekS1", "yiyecekS1", "yiyecekS1", "yiyecekS1", "yiyecekS1", "yiyecekS1", "yiyecekS1", "yiyecekS1", "yiyecekS1", "yiyecekS1",
            "yiyecekS1", "yiyecekS1", "yiyecekS1", "yiyecekS1", "yiyecekS1", "yiyecekS1", "yiyecekS1", "yiyecekS1", "yiyecekS1", "yiyecekS1", "yiyecekS1", "yiyecekS1", "yiyecekS1", "yiyecekS1", "yiyecekS1",
            "yiyecekS1", "yiyecekS1", "yiyecekS1", "yiyecekS1", "yiyecekS1", "yiyecekS1", "yiyecekS1", "yiyecekS1", "yiyecekS1", "yiyecekS1", "yiyecekS1", "yiyecekS1", "yiyecekS1", "yiyecekS1", "yiyecekS1",
            "yiyecekS1", "yiyecekS1", "yiyecekS1", "yiyecekS1", "yiyecekS1", "yiyecekS1", "yiyecekS1", "yiyecekS1", "yiyecekS1", "yiyecekS1", "yiyecekS1", "yiyecekS1", "yiyecekS1", "yiyecekS1", "yiyecekS1",
            "yiyecekS1", "yiyecekS1", "yiyecekS1", "yiyecekS1", "yiyecekS1", "yiyecekS1", "yiyecekS1", "yiyecekS1", "yiyecekS1", "yiyecekS1", "yiyecekS1", "yiyecekS1", "yiyecekS1", "yiyecekS1", "yiyecekS1",
            "yiyecekS1", "yiyecekS1", "yiyecekS1", "yiyecekS1", "yiyecekS1", "yiyecekS1", "yiyecekS1", "yiyecekS1", "yiyecekS1", "yiyecekS1", "yiyecekS1", "yiyecekS1", "yiyecekS1", "yiyecekS1", "yiyecekS1",
            "yiyecekS1", "yiyecekS1", "yiyecekS1", "yiyecekS1", "yiyecekS1", "yiyecekS1", "yiyecekS1", "yiyecekS1", "yiyecekS1", "yiyecekS1", "yiyecekS1", "yiyecekS1", "yiyecekS1", "yiyecekS1", "yiyecekS1",
            "yiyecekS1", "yiyecekS1", "yiyecekS1", "yiyecekS1", "yiyecekS1",

            "esyaS1", "esyaS1", "esyaS1", "esyaS1", "esyaS1", "esyaS1", "esyaS1", "esyaS1", "esyaS1", "esyaS1", "esyaS1", "esyaS1", "esyaS1", "esyaS1", "esyaS1", "esyaS1", "esyaS1", "esyaS1", "esyaS1", "esyaS1",
            "esyaS1", "esyaS1", "esyaS1", "esyaS1", "esyaS1", "esyaS1", "esyaS1", "esyaS1", "esyaS1", "esyaS1", "esyaS1", "esyaS1", "esyaS1", "esyaS1", "esyaS1", "esyaS1", "esyaS1", "esyaS1", "esyaS1", "esyaS1",
            "esyaS1", "esyaS1", "esyaS1", "esyaS1", "esyaS1", "esyaS1", "esyaS1", "esyaS1", "esyaS1", "esyaS1", "esyaS1", "esyaS1", "esyaS1", "esyaS1", "esyaS1", "esyaS1", "esyaS1", "esyaS1", "esyaS1", "esyaS1",
            "esyaS1", "esyaS1", "esyaS1", "esyaS1", "esyaS1", "esyaS1", "esyaS1", "esyaS1", "esyaS1", "esyaS1", "esyaS1", "esyaS1", "esyaS1", "esyaS1", "esyaS1", "esyaS1", "esyaS1", "esyaS1", "esyaS1", "esyaS1",
            "esyaS1", "esyaS1", "esyaS1", "esyaS1", "esyaS1", "esyaS1", "esyaS1", "esyaS1", "esyaS1", "esyaS1", "esyaS1", "esyaS1", "esyaS1", "esyaS1", "esyaS1", "esyaS1", "esyaS1", "esyaS1", "esyaS1", "esyaS1",
            "esyaS1", "esyaS1", "esyaS1", "esyaS1", "esyaS1", "esyaS1", "esyaS1", "esyaS1", "esyaS1", "esyaS1", "esyaS1", "esyaS1", "esyaS1", "esyaS1", "esyaS1", "esyaS1", "esyaS1", "esyaS1", "esyaS1", "esyaS1",
            "esyaS1", "esyaS1", "esyaS1", "esyaS1", "esyaS1", "esyaS1", "esyaS1", "esyaS1", "esyaS1", "esyaS1", "esyaS1", "esyaS1", "esyaS1", "esyaS1", "esyaS1", "esyaS1", "esyaS1", "esyaS1", "esyaS1", "esyaS1",
            "esyaS1", "esyaS1", "esyaS1", "esyaS1", "esyaS1", "esyaS1", "esyaS1", "esyaS1", "esyaS1", "esyaS1", "esyaS1", "esyaS1", "esyaS1", "esyaS1", "esyaS1", "esyaS1", "esyaS1", "esyaS1", "esyaS1", "esyaS1",
            "esyaS1", "esyaS1", "esyaS1", "esyaS1", "esyaS1", "esyaS1", "esyaS1", "esyaS1", "esyaS1", "esyaS1", "esyaS1", "esyaS1", "esyaS1", "esyaS1", "esyaS1", "esyaS1", "esyaS1", "esyaS1", "esyaS1", "esyaS1",
            "esyaS1", "esyaS1", "esyaS1", "esyaS1", "esyaS1", "esyaS1", "esyaS1", "esyaS1", "esyaS1", "esyaS1", "esyaS1", "esyaS1", "esyaS1", "esyaS1", "esyaS1", "esyaS1", "esyaS1", "esyaS1", "esyaS1", "esyaS1",
            "esyaS1", "esyaS1", "esyaS1", "esyaS1", "esyaS1", "esyaS1", "esyaS1", "esyaS1", "esyaS1", "esyaS1", "esyaS1", "esyaS1", "esyaS1", "esyaS1", "esyaS1", "esyaS1", "esyaS1", "esyaS1", "esyaS1", "esyaS1",
            "esyaS1", "esyaS1", "esyaS1", "esyaS1", "esyaS1", "esyaS1", "esyaS1", "esyaS1", "esyaS1", "esyaS1", "esyaS1", "esyaS1", "esyaS1", "esyaS1", "esyaS1", "esyaS1", "esyaS1", "esyaS1", "esyaS1", "esyaS1",
            "esyaS1", "esyaS1", "esyaS1", "esyaS1", "esyaS1", "esyaS1", "esyaS1", "esyaS1", "esyaS1", "esyaS1", "esyaS1", "esyaS1", "esyaS1", "esyaS1", "esyaS1", "esyaS1", "esyaS1", "esyaS1", "esyaS1", "esyaS1",
            "esyaS1", "esyaS1", "esyaS1", "esyaS1", "esyaS1", "esyaS1", "esyaS1", "esyaS1", "esyaS1", "esyaS1", "esyaS1", "esyaS1", "esyaS1", "esyaS1", "esyaS1", "esyaS1", "esyaS1", "esyaS1", "esyaS1", "esyaS1",
            "esyaS1", "esyaS1", "esyaS1", "esyaS1", "esyaS1", "esyaS1", "esyaS1", "esyaS1", "esyaS1", "esyaS1", "esyaS1", "esyaS1", "esyaS1", "esyaS1", "esyaS1", "esyaS1", "esyaS1", "esyaS1", "esyaS1", "esyaS1",
            "esyaS1", "esyaS1", "esyaS1", "esyaS1", "esyaS1", "esyaS1", "esyaS1", "esyaS1", "esyaS1", "esyaS1", "esyaS1", "esyaS1", "esyaS1", "esyaS1", "esyaS1", "esyaS1", "esyaS1", "esyaS1", "esyaS1", "esyaS1",
            "esyaS1", "esyaS1", "esyaS1", "esyaS1", "esyaS1", "esyaS1", "esyaS1", "esyaS1", "esyaS1", "esyaS1", "esyaS1", "esyaS1", "esyaS1", "esyaS1", "esyaS1", "esyaS1", "esyaS1", "esyaS1", "esyaS1", "esyaS1",
            "esyaS1", "esyaS1", "esyaS1", "esyaS1", "esyaS1", "esyaS1", "esyaS1", "esyaS1", "esyaS1", "esyaS1", "esyaS1", "esyaS1", "esyaS1", "esyaS1", "esyaS1", "esyaS1", "esyaS1", "esyaS1", "esyaS1", "esyaS1",
            "esyaS1", "esyaS1", "esyaS1", "esyaS1", "esyaS1", "esyaS1", "esyaS1", "esyaS1", "esyaS1", "esyaS1", "esyaS1", "esyaS1", "esyaS1", "esyaS1", "esyaS1", "esyaS1", "esyaS1", "esyaS1", "esyaS1", "esyaS1",
            "esyaS1", "esyaS1", "esyaS1", "esyaS1", "esyaS1", "esyaS1", "esyaS1", "esyaS1", "esyaS1", "esyaS1", "esyaS1", "esyaS1", "esyaS1", "esyaS1", "esyaS1", "esyaS1", "esyaS1", "esyaS1", "esyaS1", "esyaS1",
            "esyaS1", "esyaS1", "esyaS1", "esyaS1", "esyaS1", "esyaS1", "esyaS1", "esyaS1", "esyaS1", "esyaS1", "esyaS1", "esyaS1", "esyaS1", "esyaS1", "esyaS1", "esyaS1", "esyaS1", "esyaS1", "esyaS1", "esyaS1",
            "esyaS1", "esyaS1", "esyaS1", "esyaS1", "esyaS1", "esyaS1", "esyaS1", "esyaS1", "esyaS1", "esyaS1", "esyaS1", "esyaS1", "esyaS1", "esyaS1", "esyaS1", "esyaS1", "esyaS1", "esyaS1", "esyaS1", "esyaS1",
            "esyaS1", "esyaS1", "esyaS1", "esyaS1", "esyaS1", "esyaS1", "esyaS1", "esyaS1", "esyaS1", "esyaS1", "esyaS1", "esyaS1", "esyaS1", "esyaS1", "esyaS1", "esyaS1", "esyaS1", "esyaS1", "esyaS1", "esyaS1",
            "esyaS1", "esyaS1", "esyaS1", "esyaS1", "esyaS1", "esyaS1", "esyaS1", "esyaS1", "esyaS1", "esyaS1", "esyaS1", "esyaS1", "esyaS1", "esyaS1", "esyaS1", "esyaS1", "esyaS1", "esyaS1", "esyaS1", "esyaS1",
            "esyaS1", "esyaS1", "esyaS1", "esyaS1", "esyaS1", "esyaS1", "esyaS1", "esyaS1", "esyaS1", "esyaS1", "esyaS1", "esyaS1", "esyaS1", "esyaS1", "esyaS1", "esyaS1", "esyaS1", "esyaS1", "esyaS1", "esyaS1",
            "esyaS1", "esyaS1", "esyaS1", "esyaS1", "esyaS1", "esyaS1", "esyaS1", "esyaS1", "esyaS1", "esyaS1", "esyaS1", "esyaS1", "esyaS1", "esyaS1", "esyaS1", "esyaS1", "esyaS1", "esyaS1", "esyaS1", "esyaS1",
            "esyaS1", "esyaS1", "esyaS1", "esyaS1", "esyaS1", "esyaS1", "esyaS1", "esyaS1", "esyaS1", "esyaS1", "esyaS1", "esyaS1", "esyaS1", "esyaS1", "esyaS1", "esyaS1",

            "hayvanS1", "hayvanS1", "hayvanS1", "hayvanS1", "hayvanS1", "hayvanS1", "hayvanS1", "hayvanS1", "hayvanS1", "hayvanS1", "hayvanS1", "hayvanS1", "hayvanS1", "hayvanS1", "hayvanS1", "hayvanS1", "hayvanS1",
            "hayvanS1", "hayvanS1", "hayvanS1", "hayvanS1", "hayvanS1", "hayvanS1", "hayvanS1", "hayvanS1", "hayvanS1", "hayvanS1", "hayvanS1", "hayvanS1", "hayvanS1", "hayvanS1", "hayvanS1", "hayvanS1", "hayvanS1",
            "hayvanS1", "hayvanS1", "hayvanS1", "hayvanS1", "hayvanS1", "hayvanS1", "hayvanS1", "hayvanS1", "hayvanS1", "hayvanS1", "hayvanS1", "hayvanS1", "hayvanS1", "hayvanS1", "hayvanS1", "hayvanS1", "hayvanS1",
            "hayvanS1", "hayvanS1", "hayvanS1", "hayvanS1", "hayvanS1", "hayvanS1", "hayvanS1", "hayvanS1", "hayvanS1", "hayvanS1", "hayvanS1", "hayvanS1", "hayvanS1", "hayvanS1", "hayvanS1", "hayvanS1", "hayvanS1",
            "hayvanS1", "hayvanS1", "hayvanS1", "hayvanS1", "hayvanS1", "hayvanS1", "hayvanS1", "hayvanS1", "hayvanS1", "hayvanS1", "hayvanS1", "hayvanS1", "hayvanS1", "hayvanS1", "hayvanS1", "hayvanS1", "hayvanS1",
            "hayvanS1", "hayvanS1", "hayvanS1", "hayvanS1", "hayvanS1", "hayvanS1", "hayvanS1", "hayvanS1", "hayvanS1", "hayvanS1", "hayvanS1", "hayvanS1", "hayvanS1", "hayvanS1", "hayvanS1", "hayvanS1", "hayvanS1",
            "hayvanS1", "hayvanS1", "hayvanS1", "hayvanS1", "hayvanS1", "hayvanS1", "hayvanS1", "hayvanS1", "hayvanS1", "hayvanS1", "hayvanS1", "hayvanS1", "hayvanS1", "hayvanS1", "hayvanS1", "hayvanS1", "hayvanS1",
            "hayvanS1", "hayvanS1", "hayvanS1", "hayvanS1", "hayvanS1", "hayvanS1", "hayvanS1", "hayvanS1", "hayvanS1", "hayvanS1", "hayvanS1", "hayvanS1", "hayvanS1", "hayvanS1", "hayvanS1", "hayvanS1", "hayvanS1",
            "hayvanS1", "hayvanS1", "hayvanS1", "hayvanS1", "hayvanS1", "hayvanS1", "hayvanS1", "hayvanS1", "hayvanS1", "hayvanS1", "hayvanS1", "hayvanS1", "hayvanS1", "hayvanS1", "hayvanS1", "hayvanS1", "hayvanS1",
            "hayvanS1", "hayvanS1", "hayvanS1", "hayvanS1", "hayvanS1", "hayvanS1", "hayvanS1", "hayvanS1", "hayvanS1", "hayvanS1", "hayvanS1", "hayvanS1", "hayvanS1", "hayvanS1", "hayvanS1", "hayvanS1", "hayvanS1",
            "hayvanS1", "hayvanS1", "hayvanS1", "hayvanS1", "hayvanS1", "hayvanS1", "hayvanS1", "hayvanS1", "hayvanS1", "hayvanS1", "hayvanS1", "hayvanS1", "hayvanS1", "hayvanS1", "hayvanS1", "hayvanS1", "hayvanS1",
            "hayvanS1", "hayvanS1", "hayvanS1", "hayvanS1", "hayvanS1", "hayvanS1", "hayvanS1", "hayvanS1", "hayvanS1", "hayvanS1", "hayvanS1", "hayvanS1", "hayvanS1", "hayvanS1", "hayvanS1", "hayvanS1", "hayvanS1",
            "hayvanS1", "hayvanS1", "hayvanS1", "hayvanS1", "hayvanS1", "hayvanS1", "hayvanS1", "hayvanS1", "hayvanS1", "hayvanS1", "hayvanS1", "hayvanS1", "hayvanS1", "hayvanS1", "hayvanS1", "hayvanS1", "hayvanS1",
            "hayvanS1", "hayvanS1", "hayvanS1", "hayvanS1", "hayvanS1", "hayvanS1", "hayvanS1", "hayvanS1", "hayvanS1", "hayvanS1", "hayvanS1", "hayvanS1", "hayvanS1", "hayvanS1", "hayvanS1", "hayvanS1", "hayvanS1",
            "hayvanS1", "hayvanS1", "hayvanS1", "hayvanS1", "hayvanS1", "hayvanS1", "hayvanS1", "hayvanS1", "hayvanS1", "hayvanS1", "hayvanS1", "hayvanS1", "hayvanS1", "hayvanS1", "hayvanS1", "hayvanS1", "hayvanS1",
            "hayvanS1", "hayvanS1", "hayvanS1", "hayvanS1", "hayvanS1", "hayvanS1", "hayvanS1", "hayvanS1", "hayvanS1", "hayvanS1", "hayvanS1", "hayvanS1", "hayvanS1", "hayvanS1", "hayvanS1", "hayvanS1", "hayvanS1",
            "hayvanS1", "hayvanS1", "hayvanS1", "hayvanS1", "hayvanS1", "hayvanS1", "hayvanS1", "hayvanS1", "hayvanS1", "hayvanS1", "hayvanS1", "hayvanS1", "hayvanS1", "hayvanS1", "hayvanS1", "hayvanS1", "hayvanS1",
            "hayvanS1", "hayvanS1", "hayvanS1", "hayvanS1",

            "illerS1", "illerS1", "illerS1", "illerS1", "illerS1", "illerS1", "illerS1", "illerS1", "illerS1", "illerS1", "illerS1", "illerS1", "illerS1", "illerS1", "illerS1", "illerS1", "illerS1", "illerS1",
            "illerS1", "illerS1", "illerS1", "illerS1", "illerS1", "illerS1", "illerS1", "illerS1", "illerS1", "illerS1", "illerS1", "illerS1", "illerS1", "illerS1", "illerS1", "illerS1", "illerS1", "illerS1",
            "illerS1", "illerS1", "illerS1", "illerS1", "illerS1", "illerS1", "illerS1", "illerS1", "illerS1", "illerS1", "illerS1", "illerS1", "illerS1", "illerS1", "illerS1", "illerS1", "illerS1", "illerS1",
            "illerS1", "illerS1", "illerS1", "illerS1", "illerS1", "illerS1", "illerS1", "illerS1", "illerS1", "illerS1", "illerS1", "illerS1", "illerS1", "illerS1", "illerS1", "illerS1", "illerS1", "illerS1",
            "illerS1", "illerS1", "illerS1", "illerS1", "illerS1", "illerS1", "illerS1",

            "ulkelerS1", "ulkelerS1", "ulkelerS1", "ulkelerS1", "ulkelerS1", "ulkelerS1", "ulkelerS1", "ulkelerS1", "ulkelerS1", "ulkelerS1", "ulkelerS1", "ulkelerS1", "ulkelerS1", "ulkelerS1", "ulkelerS1",
            "ulkelerS1", "ulkelerS1", "ulkelerS1", "ulkelerS1", "ulkelerS1", "ulkelerS1", "ulkelerS1", "ulkelerS1", "ulkelerS1", "ulkelerS1", "ulkelerS1", "ulkelerS1", "ulkelerS1", "ulkelerS1", "ulkelerS1",
            "ulkelerS1", "ulkelerS1", "ulkelerS1", "ulkelerS1", "ulkelerS1", "ulkelerS1", "ulkelerS1", "ulkelerS1", "ulkelerS1", "ulkelerS1", "ulkelerS1", "ulkelerS1", "ulkelerS1", "ulkelerS1", "ulkelerS1",
            "ulkelerS1", "ulkelerS1", "ulkelerS1", "ulkelerS1", "ulkelerS1", "ulkelerS1", "ulkelerS1", "ulkelerS1", "ulkelerS1", "ulkelerS1", "ulkelerS1", "ulkelerS1", "ulkelerS1", "ulkelerS1", "ulkelerS1",
            "ulkelerS1", "ulkelerS1", "ulkelerS1", "ulkelerS1", "ulkelerS1", "ulkelerS1", "ulkelerS1", "ulkelerS1", "ulkelerS1", "ulkelerS1", "ulkelerS1", "ulkelerS1", "ulkelerS1", "ulkelerS1", "ulkelerS1",
            "ulkelerS1", "ulkelerS1", "ulkelerS1", "ulkelerS1", "ulkelerS1", "ulkelerS1", "ulkelerS1", "ulkelerS1", "ulkelerS1", "ulkelerS1", "ulkelerS1", "ulkelerS1", "ulkelerS1", "ulkelerS1", "ulkelerS1",
            "ulkelerS1", "ulkelerS1", "ulkelerS1", "ulkelerS1", "ulkelerS1", "ulkelerS1", "ulkelerS1", "ulkelerS1", "ulkelerS1", "ulkelerS1", "ulkelerS1", "ulkelerS1", "ulkelerS1", "ulkelerS1", "ulkelerS1",
            "ulkelerS1", "ulkelerS1", "ulkelerS1", "ulkelerS1", "ulkelerS1", "ulkelerS1", "ulkelerS1", "ulkelerS1", "ulkelerS1", "ulkelerS1", "ulkelerS1", "ulkelerS1", "ulkelerS1", "ulkelerS1", "ulkelerS1",
            "ulkelerS1", "ulkelerS1", "ulkelerS1", "ulkelerS1", "ulkelerS1", "ulkelerS1", "ulkelerS1", "ulkelerS1", "ulkelerS1", "ulkelerS1", "ulkelerS1", "ulkelerS1", "ulkelerS1", "ulkelerS1", "ulkelerS1",
            "ulkelerS1", "ulkelerS1", "ulkelerS1", "ulkelerS1", "ulkelerS1", "ulkelerS1", "ulkelerS1", "ulkelerS1", "ulkelerS1", "ulkelerS1", "ulkelerS1", "ulkelerS1", "ulkelerS1", "ulkelerS1", "ulkelerS1",
            "ulkelerS1", "ulkelerS1", "ulkelerS1", "ulkelerS1", "ulkelerS1", "ulkelerS1", "ulkelerS1", "ulkelerS1", "ulkelerS1", "ulkelerS1", "ulkelerS1", "ulkelerS1", "ulkelerS1", "ulkelerS1", "ulkelerS1",
            "ulkelerS1", "ulkelerS1", "ulkelerS1", "ulkelerS1", "ulkelerS1", "ulkelerS1", "ulkelerS1", "ulkelerS1", "ulkelerS1", "ulkelerS1", "ulkelerS1", "ulkelerS1", "ulkelerS1", "ulkelerS1", "ulkelerS1",
            "ulkelerS1", "ulkelerS1", "ulkelerS1", "ulkelerS1", "ulkelerS1", "ulkelerS1", "ulkelerS1", "ulkelerS1", "ulkelerS1", "ulkelerS1", "ulkelerS1", "ulkelerS1", "ulkelerS1",

            "sanatciS1", "sanatciS1", "sanatciS1", "sanatciS1", "sanatciS1", "sanatciS1", "sanatciS1", "sanatciS1", "sanatciS1", "sanatciS1",
            "sanatciS1", "sanatciS1", "sanatciS1", "sanatciS1", "sanatciS1", "sanatciS1", "sanatciS1", "sanatciS1", "sanatciS1", "sanatciS1",
            "sanatciS1", "sanatciS1", "sanatciS1", "sanatciS1", "sanatciS1", "sanatciS1", "sanatciS1", "sanatciS1", "sanatciS1", "sanatciS1",
            "sanatciS1", "sanatciS1", "sanatciS1", "sanatciS1", "sanatciS1", "sanatciS1", "sanatciS1", "sanatciS1", "sanatciS1", "sanatciS1",
            "sanatciS1", "sanatciS1", "sanatciS1", "sanatciS1", "sanatciS1", "sanatciS1", "sanatciS1", "sanatciS1", "sanatciS1", "sanatciS1",
            "sanatciS1", "sanatciS1", "sanatciS1", "sanatciS1", "sanatciS1", "sanatciS1", "sanatciS1", "sanatciS1", "sanatciS1", "sanatciS1",
            "sanatciS1", "sanatciS1", "sanatciS1", "sanatciS1", "sanatciS1", "sanatciS1", "sanatciS1", "sanatciS1", "sanatciS1", "sanatciS1",
            "sanatciS1", "sanatciS1", "sanatciS1", "sanatciS1", "sanatciS1", "sanatciS1", "sanatciS1", "sanatciS1", "sanatciS1", "sanatciS1",
            "sanatciS1", "sanatciS1", "sanatciS1", "sanatciS1", "sanatciS1", "sanatciS1", "sanatciS1", "sanatciS1", "sanatciS1", "sanatciS1",
            "sanatciS1", "sanatciS1", "sanatciS1", "sanatciS1", "sanatciS1", "sanatciS1", "sanatciS1", "sanatciS1", "sanatciS1", "sanatciS1",
            "sanatciS1", "sanatciS1", "sanatciS1", "sanatciS1", "sanatciS1", "sanatciS1", "sanatciS1", "sanatciS1", "sanatciS1", "sanatciS1",
            "sanatciS1", "sanatciS1", "sanatciS1", "sanatciS1", "sanatciS1", "sanatciS1", "sanatciS1", "sanatciS1", "sanatciS1", "sanatciS1",
            "sanatciS1", "sanatciS1", "sanatciS1", "sanatciS1", "sanatciS1", "sanatciS1", "sanatciS1", "sanatciS1", "sanatciS1", "sanatciS1",
            "sanatciS1", "sanatciS1", "sanatciS1", "sanatciS1", "sanatciS1", "sanatciS1", "sanatciS1", "sanatciS1", "sanatciS1", "sanatciS1",
            "sanatciS1", "sanatciS1", "sanatciS1", "sanatciS1", "sanatciS1", "sanatciS1", "sanatciS1", "sanatciS1", "sanatciS1", "sanatciS1",
            "sanatciS1", "sanatciS1", "sanatciS1", "sanatciS1", "sanatciS1", "sanatciS1", "sanatciS1", "sanatciS1", "sanatciS1", "sanatciS1",
            "sanatciS1", "sanatciS1", "sanatciS1", "sanatciS1", "sanatciS1", "sanatciS1", "sanatciS1", "sanatciS1", "sanatciS1", "sanatciS1",
            "sanatciS1", "sanatciS1", "sanatciS1", "sanatciS1", "sanatciS1", "sanatciS1", "sanatciS1", "sanatciS1", "sanatciS1", "sanatciS1",
            "sanatciS1", "sanatciS1", "sanatciS1", "sanatciS1", "sanatciS1", "sanatciS1", "sanatciS1", "sanatciS1", "sanatciS1", "sanatciS1",
            "sanatciS1", "sanatciS1", "sanatciS1", "sanatciS1", "sanatciS1", "sanatciS1", "sanatciS1", "sanatciS1", "sanatciS1", "sanatciS1",
            "sanatciS1", "sanatciS1", "sanatciS1", "sanatciS1", "sanatciS1", "sanatciS1", "sanatciS1", "sanatciS1", "sanatciS1", "sanatciS1",
            "sanatciS1", "sanatciS1", "sanatciS1", "sanatciS1", "sanatciS1", "sanatciS1", "sanatciS1", "sanatciS1", "sanatciS1", "sanatciS1",
            "sanatciS1", "sanatciS1", "sanatciS1", "sanatciS1", "sanatciS1", "sanatciS1", "sanatciS1", "sanatciS1", "sanatciS1", "sanatciS1",
            "sanatciS1", "sanatciS1", "sanatciS1", "sanatciS1", "sanatciS1", "sanatciS1", "sanatciS1", "sanatciS1", "sanatciS1", "sanatciS1",
            "sanatciS1", "sanatciS1", "sanatciS1", "sanatciS1", "sanatciS1", "sanatciS1", "sanatciS1", "sanatciS1", "sanatciS1", "sanatciS1",
            "sanatciS1", "sanatciS1", "sanatciS1", "sanatciS1", "sanatciS1", "sanatciS1", "sanatciS1", "sanatciS1", "sanatciS1", "sanatciS1",
            "sanatciS1", "sanatciS1", "sanatciS1", "sanatciS1", "sanatciS1", "sanatciS1", "sanatciS1", "sanatciS1", "sanatciS1", "sanatciS1",
            "sanatciS1", "sanatciS1", "sanatciS1", "sanatciS1", "sanatciS1", "sanatciS1", "sanatciS1", "sanatciS1", "sanatciS1", "sanatciS1",
            "sanatciS1", "sanatciS1", "sanatciS1", "sanatciS1", "sanatciS1", "sanatciS1", "sanatciS1", "sanatciS1", "sanatciS1", "sanatciS1",
            "sanatciS1", "sanatciS1", "sanatciS1", "sanatciS1", "sanatciS1", "sanatciS1", "sanatciS1", "sanatciS1", "sanatciS1", "sanatciS1",
            "sanatciS1", "sanatciS1", "sanatciS1", "sanatciS1", "sanatciS1", "sanatciS1", "sanatciS1", "sanatciS1", "sanatciS1", "sanatciS1",
            "sanatciS1", "sanatciS1", "sanatciS1", "sanatciS1", "sanatciS1", "sanatciS1", "sanatciS1", "sanatciS1", "sanatciS1", "sanatciS1",
            "sanatciS1", "sanatciS1", "sanatciS1", "sanatciS1", "sanatciS1", "sanatciS1", "sanatciS1", "sanatciS1", "sanatciS1", "sanatciS1",
            "sanatciS1", "sanatciS1", "sanatciS1", "sanatciS1", "sanatciS1", "sanatciS1", "sanatciS1", "sanatciS1", "sanatciS1", "sanatciS1",
            "sanatciS1", "sanatciS1", "sanatciS1", "sanatciS1", "sanatciS1", "sanatciS1", "sanatciS1", "sanatciS1", "sanatciS1", "sanatciS1",
            "sanatciS1", "sanatciS1", "sanatciS1", "sanatciS1", "sanatciS1", "sanatciS1", "sanatciS1", "sanatciS1", "sanatciS1", "sanatciS1",
            "sanatciS1", "sanatciS1", "sanatciS1", "sanatciS1", "sanatciS1", "sanatciS1", "sanatciS1", "sanatciS1", "sanatciS1", "sanatciS1",
            "sanatciS1", "sanatciS1", "sanatciS1", "sanatciS1", "sanatciS1", "sanatciS1", "sanatciS1", "sanatciS1", "sanatciS1", "sanatciS1",
            "sanatciS1", "sanatciS1", "sanatciS1", "sanatciS1", "sanatciS1", "sanatciS1", "sanatciS1", "sanatciS1",

            "sporS1", "sporS1", "sporS1", "sporS1", "sporS1", "sporS1", "sporS1", "sporS1", "sporS1", "sporS1", "sporS1", "sporS1", "sporS1", "sporS1", "sporS1", "sporS1", "sporS1", "sporS1", "sporS1", "sporS1",
            "sporS1", "sporS1", "sporS1", "sporS1", "sporS1", "sporS1", "sporS1", "sporS1", "sporS1", "sporS1", "sporS1", "sporS1", "sporS1", "sporS1", "sporS1", "sporS1", "sporS1", "sporS1", "sporS1", "sporS1",
            "sporS1", "sporS1", "sporS1", "sporS1", "sporS1", "sporS1", "sporS1", "sporS1", "sporS1", "sporS1", "sporS1", "sporS1", "sporS1", "sporS1", "sporS1", "sporS1", "sporS1", "sporS1", "sporS1", "sporS1",
            "sporS1", "sporS1", "sporS1", "sporS1", "sporS1", "sporS1", "sporS1", "sporS1", "sporS1", "sporS1", "sporS1", "sporS1", "sporS1", "sporS1", "sporS1", "sporS1", "sporS1", "sporS1", "sporS1", "sporS1",
            "sporS1", "sporS1", "sporS1", "sporS1", "sporS1", "sporS1", "sporS1", "sporS1", "sporS1", "sporS1", "sporS1", "sporS1", "sporS1", "sporS1", "sporS1", "sporS1", "sporS1", "sporS1", "sporS1", "sporS1",
            "sporS1", "sporS1", "sporS1", "sporS1"
    };
    SharedPreferences preferences;
    Boolean muzikDurumu;
    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
        progressBar = findViewById(R.id.splash_screen_processBar);
        textView = findViewById(R.id.splash_screen_textViewState);
        sorularHashmap = new HashMap<>();
        try {
            //database e gerekli tabloları oluşturuyoruz. Veritabanlarını birbiriyle ilişkili hale getirdik.
            database = this.openOrCreateDatabase("NekiBuki", MODE_PRIVATE, null);
            database.execSQL("CREATE TABLE IF NOT EXISTS Ayarlar(k_id VARCHAR,k_heart VARCHAR,k_image BLOB)");
            //cursor tanımlayarak database üzerinde işlem yapabiliyoruz.
            cursor = database.rawQuery("SELECT * FROM Ayarlar", null);
            if (cursor.getCount() < 1) {
                database.execSQL("INSERT INTO Ayarlar (k_id,k_heart) VALUES ('oyuncu','10')");
            }

            database.execSQL("CREATE TABLE IF NOT EXISTS Sorular (id INTEGER PRIMARY KEY ,skod VARCHAR UNIQUE,soru VARCHAR)");
            database.execSQL("DELETE FROM Sorular");
            sqlSorulariEkle();

            database.execSQL("CREATE TABLE IF NOT EXISTS Kelimeler (kkod VARCHAR,kelime VARCHAR,FOREIGN KEY (kkod) REFERENCES Sorular (skod))");
            database.execSQL("DELETE FROM Kelimeler");
            sqlKelimeleriEkle();

            cursor = database.rawQuery("SELECT * FROM Sorular", null);
            artacakProgres = maksimumProgres / cursor.getCount();

            int skodIndex = cursor.getColumnIndex("skod");
            int soruIndex = cursor.getColumnIndex("soru");
            textView.setText("Sorular Yükleniyor");
            //soru sayısına göre progressbarın dolma hızını ayarladık.
            while (cursor.moveToNext()) {
                sorularHashmap.put(cursor.getString(skodIndex), cursor.getString(soruIndex));
                progresmiktari += artacakProgres;
                progressBar.setProgress((int) progresmiktari);
            }
            textView.setText("Sorular Alındı, Uygulama Başlatılıyor...");
            cursor.close();

            //Intent işlemi sırasında bir saniye beklettik
            new CountDownTimer(1100, 1000) {

                @Override
                public void onTick(long millisUntilFinished) {
                }

                @Override
                public void onFinish() {
                    Intent mainIntent = new Intent(SplashScreenActivity.this, Home.class);
                    startActivity(mainIntent);
                    finish();
                }
            }.start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    //Soruları Database e ekliyoruz.
    private void sqlSorulariEkle() {
        try {
            for (int i = 0; i <= sorularList.length; i++) {
                sqlSorgusu = "INSERT INTO Sorular (skod,soru) VALUES (?,?)";
                statement = database.compileStatement(sqlSorgusu);
                statement.bindString(1, sorularKodList[i]);
                statement.bindString(2, sorularList[i]);
                statement.execute();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    //Kelimeleri Database e ekliyoruz.
    private void sqlKelimeleriEkle() {
        try {
            for (int k = 0; k <= kelimelerList.length; k++) {
                sqlSorgusu = "INSERT INTO Kelimeler (kkod,kelime) VALUES (?,?)";
                statement = database.compileStatement(sqlSorgusu);
                statement.bindString(1, kelimelerKodList[k]);
                statement.bindString(2, kelimelerList[k]);
                statement.execute();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}