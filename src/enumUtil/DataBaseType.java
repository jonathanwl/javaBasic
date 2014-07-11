package enumUtil;

public enum DataBaseType {

		MYSQL("com.mysql.jdbc.Driver", "jdbc:mysql://localhost/mydb"),    
		ORACLE("oracle.jdbc.driver.OracleDriver", "jdbc:oracle:thin:@localhost:1521:sample"),     
		DB2("com.ibm.db2.jdbc.app.DB2Driver","jdbc:db2://localhost:5000/sample"),     
		SQLSERVER("com.microsoft.jdbc.sqlserver.SQLServerDriver","jdbc:microsoft:sqlserver://localhost:1433;DatabaseName=mydb");    
		private String driver;   
		private String url;    
		//自定义的构造函数，它为驱动、URL赋值    
		DataBaseType(String driver,String url){    
			this.driver = driver;    
			this.url = url;    
			}    
		/**    * 获得数据库驱动    * @return    */  
		public String getDriver() {   
			return driver;    
		}    
		/**    * 获得数据库连接URL    * @return    */   
		public String getUrl() {   
			return url;    
		}    
		public static void main(String[] args) {
		System.out.println(DataBaseType.MYSQL);
	}

}
