# ADV Shop (eshop)

Aplikasi e-commerce sederhana menggunakan Spring Boot.  

Link deployment : https://past-atalanta-b-felesia-junelus-2406354152-c09ae512.koyeb.app/ 

---

## Modul 4 - TDD and Refactoring

### Refleksi 1

Berdasarkan implementasi Test-Driven Development (TDD) yang telah saya lakukan, saya mengevaluasi efektivitasnya berdasarkan tiga objektif dari Percival (2017) sebagai berikut:

1. Correctness (Kebenaran Program)

TDD membantu saya memikirkan skenario pengujian dengan lebih teliti untuk mendesain tes yang mencakup semua happy paths dan unhappy paths.

Sesuai panduan pertanyaan reflektif Percival, saya mengevaluasi apakah saya telah menguji seluruh kemungkinan edge cases dengan menyeluruh.

Perbaikan ke depan: Saya menyadari bahwa saya harus mulai menambahkan lebih banyak functional tests agar memiliki jaminan kuat bahwa aplikasi benar-benar bekerja dari sudut pandang pengguna, bukan hanya sekadar integrasi internal antarkomponen.

2. Maintainability (Kemudahan Pemeliharaan)

Pendekatan TDD memberikan saya rasa percaya diri dan keyakinan untuk melakukan tahapan refactoring kode dengan berani (fearlessly) dan sering.

Karena setiap perubahan struktur internal pada tahap REFACTOR secara otomatis divalidasi oleh kumpulan unit test di tahap GREEN sebelumnya, saya tidak lagi ragu untuk memodifikasi kode.

Selain itu, kebiasaan menulis pengujian lebih awal ini benar-benar membantu saya mengarahkan logika menuju struktur desain kode yang jauh lebih rapi dan tangguh.

3. Productive Workflow (Alur Kerja yang Produktif)

Meskipun memikirkan dan membuat pengujian untuk fitur yang belum eksis (pada fase RED) mungkin terasa menyita waktu pada awalnya, pada kenyataannya siklus feedback yang saya dapatkan menjadi jauh lebih cepat.

Bugs baru bisa langsung terdeteksi seketika dengan praktis. Terutama jika test ini otomatis berjalan pada pipeline CI/CD, kita bisa langsung mendeteksi ada yang salah atau bertabrakan sebelum branch di-gabungkan (merge).

Perbaikan ke depan: Agar waktu tunggu eksekusi tes tidak mengganggu alur produktivitas (productive flow state), ke depannya saya akan lebih cerdik memanfaatkan Test Doubles. Dengan menggunakan stubs untuk menggantikan pemanggilan ke modul eksternal, proses unit test bisa berjalan secara independen, sangat cepat, dan selalu konsisten (repeatable).