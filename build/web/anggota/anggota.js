/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
$(document).ready(function() {
    var na, nama, gender, tmpLahir, tglLahir, alamat, telepon, page;
        // save value from view to variable        
        function getInputValue(){
                na = $("#noAnggota").val();
	nama = $("#nama").val();
	gender = $("#gender").children("option:selected").val();
	tmpLahir = $("#tmpLahir").val();
	tglLahir = $("#tglLahir").val();
	alamat = $("#alamat").val();
	telepon = $("#telepon").val();
        }
        

        
    // get all data 
        $.ajax({
            url: "/PBO_koperasi/AnggotaCtr", 
            method: "GET", 
            dataType: "json",
            success:
                function(data){
                        $("#tabelkaryawan").dataTable({
                        serverside: true,
                        processing: true,
                        data: data,
                        sort: true,
                        searching: true,
                        paging: true,
                        columns: [
                                {'data': 'noAnggota', 'name': 'noAnggota', 'type': 'string'},
                                {'data': 'nama'},
                                {'data': 'gender'},
                                {'data': 'tmpLahir'},
                                {'data': 'tglLahir', className: 'text-center'},
                                {'data': 'telepon'},
                                {'data': null, 'className': 'dt-right', 'mRender': function(o){
                                        return "<a class='btn btn-outline-warning btn-sm'"
                                        + "id = 'btnEdit'>Edit</a>"
                                        + "&nbsp;&nbsp;"
                                        + "<a class='btn btn-outline-danger btn-sm' "
                                        + "id='btnDel''>Hapus</a>";
                                    }
                                }
                            ]
                        });

                },
                error: function(err) {
                    console.log(err);
                }
        });
        
        // procedure add data
        $("#btnAdd").click(function(){
                $("#myModal").modal('show');
                $("#titel1").show();
                $("#titel2").hide();
                $("#nik").prop('disabled', false);
                page="tambah";
        });    
        
        // save data
        $("#btnSave").on('click', function(){
    	getInputValue();
    	if (na === "") {
                        alert("Nomor Anggota Harus Diisi!!");
                        $("#noAnggota").focus();
    	}
    	else if (nama === "") {
                        alert("Nama Anggota Harus Diisi!!");
                        $("#nama").focus();
    	}
    	else{
                        
                        $.post('/PBO_koperasi/AnggotaCtr', {
    		page: page,
    		noanggota: na,
    		nama: nama,
    		gender: gender,
    		tmpLahir: tmpLahir,
    		tglLahir: tglLahir,
    		alamat: alamat,
    		telepon: telepon
                        },
                        function(data, status) {
                                alert(data);
                                if (data === "Data Berhasil diupdate" || data === "Data Berhasil disimpan") {location.reload(); }
                        });
                }
        });
        
         // procedure edit data
         $("#tabelkaryawan tbody").on("click", "#btnEdit", function() {
                $("#myModal").modal('show');
                $("#titel1").hide();
                $("#titel2").show();
                $("#noAnggota").prop('disabled', true);
                page = "tampil";
                
                let baris = $(this).closest('tr');
                let na = baris.find("td:eq(0)").text();
                $.post('/PBO_koperasi/AnggotaCtr', {
                        page: page,
                        na: na
                 },
                 function(data, status) {
                     console.log('berhasil get data by NA : ');
                     console.log(data);
                     $('#noAnggota').val(data.noAnggota);
                     $('#nama').val(data.nama);
                     $('#tmpLahir').val(data.tmpLahir);
                     $('#tglLahir').val(data.tglLahir);
                     $('#alamat').val(data.alamat);
                     $('#telepon').val(data.telepon);
                     $('#gender').val(data.gender);
                 });
                 page = "edit";
                
         });
         
         // procedure delete data
         $('#tabelkaryawan tbody').on('click', '#btnDel', function() {
             // get nik when clicked btn in the current row
             let baris = $(this).closest('tr');
             let na = baris.find("td:eq(0)").text();
             let nama = baris.find("td:eq(1)").text();
             page = 'hapus';
             
             if (confirm(`Anda yakin data  : ${na} - ${nama} akan dihapus ?`)) {
                 $.post("/PBO_koperasi/AnggotaCtr", {
                        page: page,
                        na: na
                 },
                 function(data, status) {
                     alert(data);
                     location.reload();
                 });

             }
         });
});

