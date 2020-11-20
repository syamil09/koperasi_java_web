/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
$(document).ready(function() {
    
        var noPinjaman, noAnggota, tglPinjaman, pokokPinjaman, bunga, lama, angsurPokok, angsurBunga, nikKaryawan, page, loadAnggota, loadKaryawan;
        
        // save value from view to variable        
        function getInputValue(){
                    noPinjaman = $("input[name=noPinjaman]").val();
                    noAnggota = $("input[name=noAnggota]").val();
                    tglPinjaman = $("input[name=tglPinjaman]").val();
                    pokokPinjaman = $("input[name=pokokPinjaman]").val();
                    bunga = $("input[name=bungaPinjaman]").val();
                    lama = $("input[name=lamaPinjaman]").val();
                    angsurPokok = $("input[name=angsurPokok]").val();
                    angsurBunga = $("input[name=angsurBunga]").val();
                    nikKaryawan = $("input[name=nikKaryawan]").val();
        }
        
        function loadAnggota() {
            loadAnggota = 1;
            $.ajax({
                url: "/PBO_koperasi/AnggotaCtr", 
                method: "GET", 
                dataType: "json",
                success:
                    function(data){
                            $("#tabelLookupAnggota").dataTable({
                            serverside: true,
                            processing: true,
                            data: data,
                            sort: true,
                            searching: true,
                            paging: true,
                            columns: [
                                    {'data': 'noAnggota', 'name': 'noAnggota', 'type': 'string'},
                                    {'data': 'nama'},
                                    {'data': null, 'className': 'dt-right', 'mRender': function(o){
                                            return "<a class='btn btn-warning btn-sm'"
                                            + "id = 'btnInsertAnggota'>Insert</a>";
                                        }
                                    }
                                ]
                            });

                    },
                    error: function(err) {
                        console.log(err);
                    }
            });
        }
        
        function loadKaryawan() {
            loadKaryawan = 1;
               $.ajax({
                    url: "/PBO_koperasi/KaryawanCtr", 
                    method: "GET", 
                    dataType: "json",
                    success:
                            
                        function(data){
                                $("#tabelLookupKaryawan").dataTable({
                                serverside: true,
                                processing: true,
                                data: data,
                                sort: true,
                                searching: true,
                                paging: true,
                                columns: [
                                        {'data': 'nik', 'name': 'nik', 'type': 'string'},
                                        {'data': 'nama'},
                                        {'data': null, 'className': 'dt-right', 'mRender': function(o){
                                                return "<a class='btn btn-warning btn-sm'"
                                                + "id = 'btnInsertKaryawan'>Insert</a>";
                                            }
                                        }
                                    ]
                                });

                        }
                 });
        }
        
        function getKaryawanByNIK(nik) {
            let k = 12;
            $.ajax({
                url: "/PBO_koperasi/KaryawanCtr",
                method: "GET", 
                dataType: "json",
                data: {
                   page: "tampil",
                   nik: nik
                },
                success: function(data) {
                    k = data;
                    return data;
                }
            });
            
            return k;
        }
         
        // procedure add data
        $("#btnAdd").click(function(){
                clearForm();
                $("#modalAdd").modal('show');
                $("#titel1").show();
                $("#titel2").hide();
                $("#nik").prop('disabled', false);
                $("input[name=persenBunga]").val('10');
                $("input[name=noPinjaman]").prop('disabled', false);
                page="tambah";
                console.log("add");
        });    
        
        // Lookup data anggota
        $("#btn-lookup-anggota").click(function() {
            $("#modalLookupAnggota").modal('show');
            if (loadAnggota != 1) {
                loadAnggota();
            }
            $("#tabelLookupAnggota tbody").on('click', '#btnInsertAnggota', function() {
                // get nik when clicked btn in the current row
                let baris = $(this).closest('tr');
                let no = baris.find("td:eq(0)").text();
                let nama = baris.find("td:eq(1)").text();
                $("input[name=noAnggota]").val(no);
                $("#namaAnggota").val(nama);
                $("#modalLookupAnggota").modal("hide");
            });
        });
        
        // Lookup data karyawan
        $("#btn-lookup-karyawan").click(function() {
                $("#modalLookupKaryawan").modal('show');
                if (loadKaryawan != 1) {
                    loadKaryawan();
                }
                $("#tabelLookupKaryawan tbody").on('click', '#btnInsertKaryawan', function() {
                    // get nik when clicked btn in the current row
                    let baris = $(this).closest('tr');
                    let no = baris.find("td:eq(0)").text();
                    let nama = baris.find("td:eq(1)").text();
                    $("input[name=nikKaryawan]").val(no);
                    $("#namaKaryawan").val(nama);
                    $("#modalLookupKaryawan").modal("hide");
                });
        });
        
        // save data
        $("#btnSave").on('click', function(){
    	getInputValue();
    	if (noPinjaman === "") {
                        alert("Nomor Pinjaman Harus Diisi!!");
                        $("input[name=noPinjaman]").focus();
    	}
    	else if (tglPinjaman === "") {
                        alert("Tanggal Pinjaman Harus Diisi!!");
                        $("input[name=tglPinjaman]").focus();
    	}
    	else{
                        
                        $.post('/PBO_koperasi/PinjamanCtr', {
    		page: page,
    		noPinjaman: noPinjaman,
    		noAnggota: noAnggota,
    		tglPinjaman: tglPinjaman,
                                lamaPinjaman: lama,
    		pokokPinjaman: pokokPinjaman,
    		bungaPinjaman: bunga,
    		angsurPokok: angsurPokok,
    		angsurBunga: angsurBunga,
                                nikKaryawan: nikKaryawan
                        },
                        function(data, status) {
                                alert(data);
                                if (data === "Data Berhasil diupdate" || data === "Data Berhasil disimpan") {location.reload(); }
                        });
                }
        });

        $("#btnCancel").on('click', function() {
    	$("#myModal").modal('hide');
    });

    // get all data 
    $.ajax({
        url: "/PBO_koperasi/PinjamanCtr", 
        method: "GET", 
        dataType: "json",
        success:
            function(data){
                    $("#tabelpinjaman").dataTable({
                    serverside: true,
                    processing: true,
                    data: data,
                    sort: true,
                    searching: true,
                    paging: true,
                    columns: [
                            {'data': 'noPinjaman'},
                            {'data': 'noAnggota'},
                            {'data': 'namaAnggota'},
                            {'data': 'tglPinjaman'},
                            {'data': 'pokokPinjamanRp'},
                            {'data': 'bungaPinjamanRp'},
                            {'data': 'lamaPinjaman'},
                            {'data': 'angsurPokokRp'},
                            {
                                data: 'angsurBunga',
                                defaultContent: '',
                                render: {
                                    display: function(data, type , row) {
                                        return formatRupiah(data.toString(), 'Rp. ');
                                    }
                                }
                            },
                            {'data': 'namaKaryawan',  'searchable': false},
                            {'data': null, 'className': 'dt-right', 'orderable': false, 'mRender': function(o){
                                    return "<a class='btn btn-outline-warning btn-sm'"
                                    + "id = 'btnEdit'>Edit</a>"
                                    + "&nbsp;&nbsp;"
                                    + "<a class='btn btn-outline-danger btn-sm' "
                                    + "id='btnDel'>Hapus</a>";
                                }
                            }
                        ]
                    });
                
            }
         });
         
         // procedure delete data
         $('#tabelpinjaman tbody').on('click', '#btnDel', function() {
             // get nik when clicked btn in the current row
             let baris = $(this).closest('tr');
             let np = baris.find("td:eq(0)").text();
             let nama = baris.find("td:eq(1)").text();
             page = 'hapus';
             console.log(np);
             if (confirm(`Anda yakin data  : ${np} akan dihapus ?`)) {
                 $.post("/PBO_koperasi/PinjamanCtr", {
                        page: page,
                        noPinjaman: np
                 },
                 function(data, status) {
                     alert(data);
                     location.reload();
                 });

             }
         });
         
         // procedure edit data
         $("#tabelpinjaman tbody").on("click", "#btnEdit", function() {
                $("#modalAdd").modal('show');
                $("#titel1").hide();
                $("#titel2").show();
                $("input[name=noPinjaman]").prop('disabled', true);
                page = "tampil";
                
                let baris = $(this).closest('tr');
                let np = baris.find("td:eq(0)").text();
                $.post('/PBO_koperasi/PinjamanCtr', {
                        page: page,
                        noPinjaman: np
                 },
                 function(data, status) {
                     console.log(data);
                        $("input[name=noPinjaman]").val(data.noPinjaman);
                        $("input[name=noAnggota]").val(data.noAnggota);
                        $("#namaAnggota").val(data.namaAnggota);
                        $("input[name=tglPinjaman]").val(data.tglPinjaman);
                        $("input[name=pokokPinjaman]").val(data.pokokPinjaman);
                        $("input[name=bungaPinjaman]").val(data.bungaPinjaman);
                        $("input[name=lamaPinjaman]").val(data.lamaPinjaman);
                        $("input[name=angsurPokok]").val(data.angsurPokok);
                        $("input[name=angsurBunga]").val(data.angsurBunga);
                        $("input[name=nikKaryawan]").val(data.accPetugas);
                        $("#namaKaryawan").val(data.namaKaryawan);
                        $("#pokokPinjamanRp").val(formatRupiah(data.pokokPinjaman.toString(), "Rp. "));
                        $("#bungaPinjamanRp").val(formatRupiah(data.bungaPinjaman.toString(), "Rp. "));
                        $("#angsurPokokRp").val(formatRupiah(data.angsurPokok.toString(), "Rp. "));
                        $("#angsurBungaRp").val(formatRupiah(data.angsurBunga.toString(), "Rp. "));
                 });
                 page = "edit";
                
         });
         
         /* Fungsi formatRupiah */
        function formatRupiah(angka, prefix) {
          var number_string = angka.replace(/[^,\d]/g, "").toString(),
            split = number_string.split(","),
            sisa = split[0].length % 3,
            rupiah = split[0].substr(0, sisa),
            ribuan = split[0].substr(sisa).match(/\d{3}/gi);

          // tambahkan titik jika yang di input sudah menjadi angka ribuan
          if (ribuan) {
            separator = sisa ? "." : "";
            rupiah += separator + ribuan.join(".");
          }

          rupiah = split[1] != undefined ? rupiah + "," + split[1] : rupiah;
          return prefix == undefined ? rupiah : rupiah ? "Rp. " + rupiah : "";
        }
        
        // fungsi angsuran pokok
        function angsuranPokok(pokok, lama) {
            // bulatkan keatas dalam bentuk ratusan
            let angsur = Math.ceil(parseInt(pokok) / parseInt(lama) / 100) * 100;
            if (!isNaN(angsur) && isFinite(angsur)) {
                $("input[name=angsurPokok]").val(angsur);
                $("#angsurPokokRp").val(formatRupiah(angsur.toString(), "Rp. "));
            } else {
                $("input[name=angsurPokok]").val('');
                $("#angsurPokokRp").val(formatRupiah(angsur.toString(), "Rp. "));
            }    
            console.log('angsuran pokok : '+angsur)
        }
        
        // fungsi angsuran bunga
        function angsuranBunga(bunga, lama) {
            // bulatkan keatas dalam bentuk ratusan
            let angsur = Math.ceil(parseInt(bunga) / parseInt(lama) / 100) * 100;
            if (!isNaN(angsur) && isFinite(angsur) && angsur != 0) {
                $("input[name=angsurBunga]").val(angsur);
                $("#angsurBungaRp").val(formatRupiah(angsur.toString(), "Rp. "));
            } else {
                $("input[name=angsurBunga]").val('');
                $("#angsurBungaRp").val('');
            }
            console.log('angsur bunga : '+angsur);
        }
        
        // fungsi pokok bunga
        function pokokBunga(pokok, lama, persen) {
            let bunga = Math.ceil( (lama/12) * pokok * (persen/100) );
//            let bunga = ( (lama/12) * pokok * (persen/100) );
//            bunga = Number.isInteger(bunga) ? bunga : bunga.toFixed(2); 
            
            if (!isNaN(bunga) && isFinite(bunga) && bunga != 0) {
                $("input[name=bungaPinjaman]").val(bunga);
                $("#bungaPinjamanRp").val(formatRupiah(bunga.toString().replace('.', ','), "Rp. "));
                angsuranBunga(bunga, lama);
            } else {
                $("input[name=bungaPinjaman]").val('');
                $("#bungaPinjamanRp").val('');
                $("input[name=angsurBunga]").val('');
                $("#angsurBungaRp").val('');
            }
            // jalankan fungsi angsuran pokok
            angsuranPokok(pokok, lama);
            console.log("bunga : "+bunga);
        }
        
        $("input[name=persenBunga]").on('input', function() {
            pokokBunga(
                    $("input[name=pokokPinjaman]").val(),
                    $("input[name=lamaPinjaman]").val(),
                    this.value
                    );
        });
        
        $("input[name=pokokPinjaman]").on('input', function() {
            let rp = formatRupiah(this.value, "RP. ");
            $("#pokokPinjamanRp").val(rp);
//            angsuranPokok($("input[name=pokokBunga]").val(), $("input[name=lamaPinjaman]").val());
            pokokBunga(
                    this.value,
                    $("input[name=lamaPinjaman]").val(),
                    $("input[name=persenBunga]").val()
                    );
        });
        
        $("input[name=lamaPinjaman]").on('input', function() {
            pokokBunga(
                    $("input[name=pokokPinjaman]").val(),
                    this.value,
                    $("input[name=persenBunga]").val()
                    );
        });
        
        // fill name karyawan
        $("input[name=nikKaryawan]").blur(function() {
            console.log(this.value)
            $.ajax({
                url: "/PBO_koperasi/KaryawanCtr",
                method: "GET", 
                dataType: "json",
                data: {
                   page: "tampil",
                   nik: this.value
                },
                success: function(data) {
                    if (Object.keys(data).length !== 0 && data.constructor === Object) $("#namaKaryawan").val(data.nama);
                    else $("#namaKaryawan").val('Data tidak ditemukan!');
                }
            });
        });
        
        // fill name  anggota
        $("input[name=noAnggota]").blur(function() {
            console.log(this.value)
            $.ajax({
                url: "/PBO_koperasi/AnggotaCtr",
                method: "GET", 
                dataType: "json",
                data: {
                   page: "tampil",
                   na: this.value
                },
                success: function(data) {
                    if (data != null) $("#namaAnggota").val(data.nama);
                    else $("#namaAnggota").val('Data tidak ditemukan!');           
                }
            });
        });
         
         function clearForm() {
                    $("input[name=noPinjaman]").val('');
                    $("input[name=noAnggota]").val('');
                    $("input[name=tglPinjaman]").val('');
                    $("input[name=pokokPinjaman]").val('');
                    $("input[name=bungaPinjaman]").val('');
                    $("input[name=lamaPinjaman]").val('');
                    $("input[name=angsurPokok]").val('');
                    $("input[name=angsurBunga]").val('');
                    $("input[name=nikKaryawan]").val('');
                    $("#namaKaryawan").val('');
                    $("#namaAnggota").val('');
                    $("#bungaPinjamanRp").val('');
                    $("#pokokPinjamanRp").val('');
                    $("#angsurBungaRp").val('');
                    $("#angsurPokokRp").val('');
         }
});






