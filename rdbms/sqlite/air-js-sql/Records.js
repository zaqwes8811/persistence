/**
	class ���������� �������
*/
function SimpleRecord(){
	this.Date = null;
	this.Type = null;
	this.TextContent = null;
	this.LinkOut = null;
	this.id = null;
	// ('"+Date+"', '"+Type+"', '"+TextContent+"', '"+LinkOut+"')"
	this.getInsertString = function(){
		return " ('"+this.Date+"', '"+	// ������� ���� �����!
			this.Type+"', '"+
			this.TextContent+"', '"+
			this.LinkOut+"')";
	}
}
