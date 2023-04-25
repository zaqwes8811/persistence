/**
	1. ���������(���������)/��������� ���� ������
	2. ������ ������ - �� ����� ����� ������, �� ��� ����. �����
		�� ������ ��� �� ������, �� � ��������� ������
		! ���� ������ ������, �� ������� ��������� �������
			����� ���� � ������� ��� ������� � �����!
	3. ������ ������� - �� ������ ������ ��������!
	
	[ ������� ����, ����. ���-�� ]
	thi : ���� ��� ����� ���������, �� ����� ������� �����, � �� ��������� �������
	�������������� ��� ������ ���������
		��� ������� ���������� ����� prim key
		��� �������� ������ ����
	�������..? - ��� ��� ��������� ������ �����
		� ui ����� ����� �������, ����� ����� ��������� ���� ����� ��� ��������
			������ ����� ������������� � ui
	����� �������� ��� ������� ����� ������ �� ���������� �����
*/
// ������� ��� ���������� ������������
function extend(Child, Parent) {
	var F = function() { }
	F.prototype = Parent.prototype
	Child.prototype = new F()
	Child.prototype.constructor = Child
	Child.superclass = Parent.prototype
}
// ���������� ��. ��� �������� ������ ���, ��
//   ��� ������� ������� ���, � ����� ������ ������ ��������
function ISQLite(){
	this.open = function( url ){}
	this.createTbl = function( tblName ){}
	
	// ������� ����������� ������� - ������ ����� 
	this.insertRecordInto = function( record, tblName ){}
	
	// ������ ���������� ������ ������� � ���������� �������
	this.selectFromLike = function(tblName, key){}
	
	//
	//this.resetLayer =
	//this.handleValid
}
/**
	abs. : ���������� �������� � ����� ������
*/
function AirSQLiteSync() {
	this._db = null
	this.open = function( url ){
		// ����������� � ������
		var file = new air.File( url ); 

		// ������� ����������
		this._db = new air.SQLConnection();
		
		// ���������� ������ � ���������� ���� ������
		var result = false;

		// ��������� ��������� ������� ��������
		try {
			// ����� ���� � ���������� ������
			this._db.open( file, air.SQLMode.CREATE );
			result = true;
		}
		catch(SQLError) {
			air.trace( 'SQLError : check path to url' );
		}
		return result;
	}
	this.close = function() {
		try {
			// ����� ���� � ���������� ������
			this._db.close(  );
		}
		catch(SQLError) {
			air.trace( 'SQLError : check path to file' );
		}
	}
	this.createTbl = function ( tbl ) {
		// Date[str = 00 00 00 00 00] // �������� ��� �� �������� ����� ��� ������. �����������
		// Type[do/thi/] 
		// TextContent[str] 
		// LinkOut ( ���� ���������� ����� )
		var stmtCreate = new air.SQLStatement();
		stmtCreate.sqlConnection = this._db;
		stmtCreate.text = "CREATE TABLE IF NOT EXISTS "+tbl+" ( " +
			'id INTEGER PRIMARY KEY AUTOINCREMENT, ' +
			'Date TEXT, ' +
			'Type TEXT, ' +
			'TextContent TEXT, ' +
			'LinkOut TEXT )';
		try {
			stmtCreate.execute();
		}
		catch(SQLError){
			air.trace('SQLError : CREATE TABLE')
		}
	}
	this.insertRecordInto = function ( tbl, record ) {
		var stmt = new air.SQLStatement();
		stmt.sqlConnection = this._db;
		
		// ������ ������
		stmt.text = 
			"INSERT INTO "+tbl+" (Date, Type, TextContent, LinkOut) " +  
			"VALUES"+record.getInsertString();
		try {
			stmt.execute();
			var result = stmt.getResult(); 
			record.id = result.lastInsertRowID; 
		}
		catch(SQLError){
			air.trace('SQLError : INSERT '+SQLError.message)
		}
	}
	this.selectFromLike = function ( tbl, likeString ) {
		var stmt = new air.SQLStatement();
		stmt.sqlConnection = this._db;
		stmt.text = "SELECT * FROM "+tbl+" "+"WHERE Date LIKE '"+likeString+"'";
		var resultList = new Array();
		try {
			stmt.execute();
			var result = stmt.getResult(); 
			var numResults = result.data.length; 
			for (var i = 0; i < numResults; i++) {
				var row = result.data[i]; 
				var record = new SimpleRecord();
				record.id = row.id; 
				record.Date = row.Date; 
				record.Type = row.Type; 
				record.TextContent = row.TextContent; 
				record.LinkOut = row.LinkOut; 
				resultList.push( record );
			}
		}
		catch(SQLError){
			// ���� �� �������� ���� ������� ���� ������
			air.trace( 'SQLError : SELECT '+SQLError.message)
		}
		return resultList;
	}
}
extend(AirSQLiteSync, ISQLite)

// Run()
function Run(){
	// ������� ���������� ����� � g_pwd ��� �� ����������
	//var g_pwd = "D:\\Public\\do-small\\Dropbox\\bitbucket\\air-planner\\Planthi-app-branch"
	var g_pwd = "D:\\";
	var url = g_pwd+"testAir.db";
	var airBase = new AirSQLiteSync();
	
	// ��������� ���� ������
	var err = airBase.open( url );
	
	// ������� ������� ���� �� ���
	var tblName = 'AllRecords';
	airBase.createTbl( tblName );
	
	// ! ������
	var Date = "%1208311500";
	var Type = "do";
	var resultList = airBase.selectFromLike( tblName, Type );
	// ������������� �� ���� ���� ���� ���������� ������
	//createCalendarGrid( recordsList )
	//plotGraph( recordsList );
	
	// ������� ������
	var record = new SimpleRecord();
	record.Date = "1208311500";
	record.Type = "do";
	record.TextContent = "testRecord";
	record.LinkOut = "1208311501";

	// ���� ������� ������ � ����
	airBase.insertRecordInto( tblName, record );
	air.trace( record.id );	// ��� �����
	
	// RAII �� ���������� �����!
	airBase.close();
}
var g_callsArray

// �������� �������� � ����������
function createCalendarGrid( recordsList ) {
	// ������� ������� ���� ��� ����
	var mydiv = document.getElementById("boxCentral");
	while ( mydiv.firstChild ) mydiv.removeChild( mydiv.firstChild );

	// �������
	var arrayOfCells = new Array()
	var len = recordsList.length

	// ��������� ��������
	// ��� ���� ���� �� �������, ����� ����� ������! ��������� � ���� � ������ - ��� �� �����
	for( var i = 0; i < len; i++ ) {
		// ������� ����� ���
		var newCell = document.createElement('div')

		// ���������
		newCell.align="middle"
		newCell.id = "it_Div"	// ����� ����� Id ��� ����������!!
		newCell.innerHTML = recordsList[i].Date

		// ������� ���������� ������
		newCell.linkObj = recordsList[i];

		// ���������� �����������
		newCell.onclick = onClickCell

		// ��� ���������� ��������� ����
		arrayOfCells.push(newCell)  
	}

	// ���������� (����� ����������)
	for( var i = 0; i < len; i++ ) {
		// ��������� � �������������
		document.getElementById("boxCentral").appendChild( arrayOfCells[i] )
	}

	//document.getElementById("boxCentral").appendChild(newTable)
	// ���������� ������ ������ ��� ������������ � ��������
	return arrayOfCells
}
/**
	���������� ����� �� ��������
*/
function onClickCell(event){
	event = event || window.event
	// �����-��������� �������� target
 	var t = event.target || event.srcElement
	 
	// ������ ����� ������ ������ ( ���� ������ ����� � ���
	air.trace( t.linkObj.id )
}

// ������ ����� �������� ����������, ����� ���� � �������
//   ���������� ������ ����� ������������
$(window).load(function () {
	Run();
});
$(document).ready(function(){
	$("#btClose").mouseenter(function(){
		$(this).css('background-image','-webkit-gradient(radial, 50% 50%, 0, 50% 50%,20, from(#ffffff), to(red))');
		// background-image: -webkit-gradient(radial, 50% 50%, 0, 50% 50%,100, from(#cde6f9), to(#6B86A6));
	});
	$("#btClose").mouseout(function(){
		//
		$(this).css('background-image','-webkit-gradient(radial, 50% 50%, 0, 50% 50%,20, from(#red), to(red))');
	});
});
