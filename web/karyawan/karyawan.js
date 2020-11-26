/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
$(document).ready(function() {
    
        var nik, nama, gender, tmpLahir, tglLahir, alamat, telepon, page;
        
        // save value from view to variable        
        function getInputValue(){
                nik = $("#nik").val();
	nama = $("#nama").val();
	gender = $("#gender").children("option:selected").val();
	tmpLahir = $("#tmpLahir").val();
	tglLahir = $("#tglLahir").val();
	alamat = $("#alamat").val();
	telepon = $("#telepon").val();
        }
        
        // procedure add data
        $("#btnAdd").click(function(){
                $("#myModal").modal('show');
                $("#titel1").show();
                $("#titel2").hide();
                $("#nik").prop('disabled', false);
                page="tambah";
                console.log("add");
        });    
        
        // save data
        $("#btnSave").on('click', function(){
    	getInputValue();
    	if (nik === "") {
                        alert("Nomor Induk Karyawan Harus Diisi!!");
                        $("#nik").focus();
    	}
    	else if (nama === "") {
                        alert("Nama Karyawan Harus Diisi!!");
                        $("#nama").focus();
    	}
    	else{
                        
                        $.post('/PBO_koperasi/KaryawanCtr', {
    		page: page,
    		nik: nik,
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

        $("#btnCancel").on('click', function() {
    	$("#myModal").modal('hide');
    });

    // get all data 
    $.ajax({
        url: "/PBO_koperasi/KaryawanCtr", 
        method: "GET", 
        dataType: "json",
        success:
            function(data){
                    $("#tabelkaryawan").DataTable({
                    serverside: true,
                    processing: true,
                    data: data,
                    sort: true,
                    searching: true,
                    paging: true,
                    columns: [
                            {'data': 'nik', 'name': 'nik', 'type': 'string'},
                            {'data': 'nama'},
                            {'data': 'gender'},
                            {'data': 'tmpLahir'},
                            {'data': 'tglLahir', className: 'text-center'},
                            {'data': 'telepon'},
                            {'data': null, 'className': 'dt-right', 'mRender': function(o){
                                    return "<a class='btn btn-outline-warning btn-sm'"
                                    + "id = 'btnEdit' href='#'>Edit</a>"
                                    + "&nbsp;&nbsp;"
                                    + "<a class='btn btn-outline-danger btn-sm' "
                                    + "id='btnDel' href='#'>Hapus</a>";
                                }
                            }
                        ]
                    });
                
            }
         });
         
         // procedure delete data
         $('#tabelkaryawan tbody').on('click', '#btnDel', function() {
             // get nik when clicked btn in the current row
             let baris = $(this).closest('tr');
             let nik = baris.find("td:eq(0)").text();
             let nama = baris.find("td:eq(1)").text();
             page = 'hapus';
             
             if (confirm(`Anda yakin data  : ${nik} - ${nama} akan dihapus ?`)) {
                 $.post("/PBO_koperasi/KaryawanCtr", {
                        page: page,
                        nik: nik
                 },
                 function(data, status) {
                     alert(data);
                     location.reload();
                 });

             }
         });
         
         // procedure edit data
         $("#tabelkaryawan tbody").on("click", "#btnEdit", function() {
                $("#myModal").modal('show');
                $("#titel1").hide();
                $("#titel2").show();
                $("#nik").prop('disabled', true);
                page = "tampil";
                
                let baris = $(this).closest('tr');
                let nik = baris.find("td:eq(0)").text();
                $.post('/PBO_koperasi/KaryawanCtr', {
                        page: page,
                        nik: nik
                 },
                 function(data, status) {
                     console.log(data);
                     $('#nik').val(data.nik);
                     $('#nama').val(data.nama);
                     $('#tmpLahir').val(data.tmpLahir);
                     $('#tglLahir').val(data.tglLahir);
                     $('#alamat').val(data.alamat);
                     $('#telepon').val(data.telepon);
                     $('#gender').val(data.gender);
                 });
                 page = "edit";
                
         });
         
});




