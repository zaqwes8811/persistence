
// form site QUnit
$(document).ready(function(){
	function testReadingTxtFile() {
		/**
			���� ����� ������������� � xml ������ - ���������, ����� ����� ����� �����!!
			���� ������� � �� ��� ������� ��� - ��� ������ ����� ������ �� ��������, �������
				����������
		*/
		var urlStr = g_pwd+"\\idat\\planthi.xml"; 
		var file = new air.File(urlStr); 
		var fileStream = new air.FileStream(); 
		fileStream.open(file, air.FileMode.READ);
		
		// ������ ����������
		var fileContent = fileStream.readMultiByte(fileStream.bytesAvailable, 'utf-8');
		fileContent = fileContent.replace(/Ref/g,'ref')
		fileContent = fileContent.replace(/Think/g,'thi')
		fileContent = fileContent.replace(/Do/g,'do')
		fileContent = fileContent.replace(/Learn/g,'lrn')
		fileContent = fileContent.replace(/Thi/g,'thi')
		fileContent = fileContent.replace(/Anl/g,'anl')
		
		// ��������� ���-���� �������� ���������� �����������
		var ID = "ref"
		fileContent = processOneType(ID, fileContent);
		var ID = "thi"
		fileContent = processOneType(ID, fileContent);
		var ID = "do"
		fileContent = processOneType(ID, fileContent);
		var ID = "anl"
		fileContent = processOneType(ID, fileContent);
		var ID = "lrn"
		fileContent = processOneType(ID, fileContent);
		
		// ������ ����� ����� ������ �� ������ ����� ':'
		fileContent = fileContent.replace(/\:/g,'')

		// �������
		fileStream.close()
		
		// ����� - � �������� ������� ������� ����� ����������
		var urlStrWr = g_pwd+"\\odat\\planthi_mon.xml"; 
		var fileWr = new air.File(urlStrWr); 
		var fileStreamWr = new air.FileStream(); 
		fileStreamWr.open(fileWr, air.FileMode.WRITE);
		
		// 	
		fileStreamWr.writeMultiByte(fileContent, 'utf-8')	// �������� ��� ��� �������� �����
		fileStreamWr.close()
		
		// ����� ����� ���� ����� �� �������� ����
		equal( false, false, "Right path" );
	}
	test( "Open and close database",testReadingTxtFile );
})


});