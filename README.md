# ADV Shop (eshop)

Aplikasi e-commerce sederhana menggunakan Spring Boot.  

Link deployment : https://past-atalanta-b-felesia-junelus-2406354152-c09ae512.koyeb.app/ 

---

## Modul 4 - TDD and Refactoring

### Refleksi 1: Pemahaman Test-Driven Development (TDD) berdasarkan Percival (2017)

Berdasarkan implementasi Test-Driven Development (TDD) yang telah saya lakukan dengan alur RED-GREEN-REFACTOR, saya mengevaluasi efektivitasnya berdasarkan panduan obyektif *Evaluating Your Testing Objectives* dari Percival (2017) sebagai berikut:

**1. Apakah Alur TDD Ini Bermanfaat?**
Secara keseluruhan, alur TDD **sangat bermanfaat** dan mempermudah pengembangan aplikasi karena memenuhi tujuan pengujian:
*   **Mencegah Bug Tembus ke Produksi (Correctness):** Menulis tes sebelum kode (pada fase *RED*) "memaksa" saya memikirkan spesifikasi ekspekstasi (*happy path*) serta kemungkinan kondisi gagal (*edge case* / *unhappy path*) sejak awal (contoh: format `voucherCode` yang salah atau `bankName` yang kosong). Hal ini melatih pemahaman saya mengenai *business rules* sistem sehingga meminimalisir peluang masuknya *bug*.
*   **Mendokumentasikan Perilaku Sistem (Documentation):** *Unit tests* yang saya buat secara praktis bertindak sebagai "dokumentasi hidup" (*living documentation*). Pemrogram lain dapat membaca `PaymentServiceImplTest` untuk mengerti logika validasi `BANK_TRANSFER` atau `VOUCHER_CODE` tanpa perlu menerka-nerka dari kode sumber utamanya secara langsung.
*   **Meningkatkan Rasa Aman saat Refactoring (Maintainability):** Siklus TDD memberikan saya jaring pengaman (*safety net*). Sesuai janji tahapan *REFACTOR*, saya tidak ragu memperbaiki maupun merombak internal struktur kode lama (misal: menggantikan `order.setStatus` dengan `orderService.updateStatus`) dengan sangat berani (*fearlessly*) karena saya tahu persis tes-tes saya di siklus *GREEN* sebelumnya akan men-validasi fungsionalitasnya seketika.

**2. Hal yang Perlu Diperbaiki ke Depannya:**
Meskipun pengujian ini sangat berguna bagi saya, kelemahan TDD yang terkadang saya alami adalah terjebak pada **"mendesain tes yang terlalu bergantung pada rincian implementasi"** (*tightly coupled*). Terkadang saya terlalu ketat memverifikasi hal internal (seperti spesifik metode *repository* dipanggil persis berapa kali) hanya demi metrik *coverage* 100%. Di waktu berikutnya, saya harus:
*   Berfokus menguji **perilaku** antarmuka (Input $\rightarrow$ Output) program, alih-alih mengecek cara khusus (*How*) kelas tersebut menunaikan tugas mandirinya secara berlebihan.
*   Meningkatkan bobot komposisi *Functional Tests*, agar memiliki jaminan lebih meyakinkan bahwa aplikasi benar-benar bekerja memukau dari kacamata peramban pengguna akhir (bukan hanya tes integrasi kelas).

---

### Refleksi 2: Pemahaman Prinsip F.I.R.S.T pada Kode Pengujian Saya

Konsep F.I.R.S.T adalah landasan utama dalam mengukur seberapa efisien kode tes (khususnya *unit test*) yang ditulis. Bedasarkan *unit test* dan *functional test* yang telah saya terapkan pada exercise ini, berikut evaluasinya:

*   **Fast (Cepat): Terpenuhi.** Hampir semua *unit test* saya untuk `Controller` dan `Service` menggunakan anotasi `@ExtendWith(MockitoExtension.class)` serta Mockito (`@Mock`, `@InjectMocks`). Akibatnya, tes dapat diisolasi dan menipu (*stub*) dependensi ke pangkalan data atau objek berat (*Repository*) lainnya. Ini menyebabkan pengujian saya selesai dieksekusi kurang dari sekian milidetik, memberikan alur balasan peringatan (*feedback loop*) yang instan bagi produktivitas saya.
*   **Independent (Independen/Terisolasi): Sebagian Terpenuhi.** Secara garis besar tes saya sudah terisolasi karena dibantu oleh mekanisme `@BeforeEach` yang senantiasa "mereset ulang" instansi kelas (`Payment`, `Order`) menjadi bersih dari nol sebelum sebuah tes baru mengambil giliran eksekusi. **Namun**, dalam ranah fungsional tes (*Selenium WebDriver*), terkadang data yang ditulis dan belum dihapus permanen oleh satu tes fungsional bisa membocorkan pengaruh "*state*" ke tes lain apabila urutannya dimainkan secara asinkron/acak.
*   **Repeatable (Dapat Diulangi): Terpenuhi.** Saya telah merancang tes supaya berjalan konsisten di lingkungan mana saja. Bahkan Github Actions _Continuous Integration_ (CI) mampu berulang kali memvalidasi pengujian ini dengan sukses di Linux *runner*-nya tanpa masalah sistem operasi atau deviasi lingkungan (berkat konfigurasi Gradle lokal yang rapi).
*   **Self-Validating (Memvalidasi Dirinya Sendiri): Terpenuhi.** Tidak ada instrumen *Print/System.out* manual untuk memvalidasi benar salahnya hasil program saya. Tiap tes menggunakan metode pernyataan (*assertions*) keras (`assertEquals`, `assertTrue`, `assertThrows`, `verify`). Segala macam keputusan LULUS (*passed*) atau GAGAL (*failed*) diputuskan secara otonomis dengan rekapitulasi laporannya berwarna Hijau/Merah (*Boolean logic*).
*   **Timely (Tepat Waktu/Singkron Alur): Perlu Diperbaiki.** Saya meniatkan diri untuk menulis tes mendahului fungsi yang mengandalkannya (_Test-First_), akan tetapi di dunia nyata terkadang ritme godaan saya tetap condong menyempurnakan implementasi dasarnya lebih dahulu lantaran belum tahu persis struktur akhirnya. 

**Perbaikan untuk Test di Masa Depan:**
*   Untuk menambal masalah tes yang *kurang* Independen khususnya pada sisi Functional Testing, saya akan membiasakan diri menulis mekanisme "*data wiping*" atau "*teardown*" (misal melalui blok `@AfterEach`) guna membersihkan data persisten/lokal sementara usai sebuah blok pengetesan fungsional tutup usia, atau minimal menggunakan *in-memory runtime transient* secara eksklusif.
*   Serta menahan ego untuk implementasi lebih dahulu sebelum *Timely* disiplin mendesain kerangka tes yang diharapkan (*Red-phase first*).