$(document).ready(function() {
	 
	 var tableCount = 0;
	 loader(tableCount);
	 /* Loading First Table Data */
     $('#userTable').dataTable( {
    	 
    	 "scrollX": true,
    	 "columnDefs": [
 	              { width: '130', targets: [0,1,2,3,4,6,7,9,10,11,12,13] },
 	              { width: '300', targets: [5,8] },
 	          ],
         "ajax": {
           "url":serverResourceURL,
           "dataSrc": ""
          },
          "initComplete": function(settings, json) {
        	  tableCount = tableCount+1;
        	  loader(tableCount);
        	  },
         "columns": [
             { "data": "orderDate" },
             { "data": "poNumber" },
             { "data": "channelName" },
             { "data": "orderStatus" },
             { "data": "sku" },
             { "data": "productName" },
             { "data": "quantity" },
             { "data": "orderTotal" },
             { "data": "supplierCostPerSku" },
             { "data": "salesPrice" },
             { "data": "amazonAmount" },
             { "data": "extNetUnit" },
             { "data": "profit" },
             { "data": "roi" }
         ]
     } );
     /* End Loading First Table Data */
     
     /* Loading Second Table Data */
     $('#userSecoundTable').dataTable( {
     	"scrollX": true,
     	"columnDefs": [
	 	              { width: '130', targets: [0,1,2,3,4,6,7,9,10,11,12,13] },
	 	              { width: '300', targets: [5,8] },
	 	    ],
         "ajax": {
           "url":downloadMoreThanOneURL,
           "dataSrc": ""
          },
          "initComplete": function(settings, json) {
        	  tableCount = tableCount+1;
        	  loader(tableCount);
      	  },
         "columns": [
             { "data": "orderDate" },
             { "data": "poNumber" },
             { "data": "channelName" },
             { "data": "orderStatus" },
             { "data": "sku" },
             { "data": "productName" },
             { "data": "quantity" },
             { "data": "orderTotal" },
             { "data": "supplierCostPerSku" },
             { "data": "salesPrice" },
             { "data": "amazonAmount" },
             { "data": "extNetUnit" },
             { "data": "profit" },
             { "data": "roi" }
         ]
     } );
     /* End Loading Second Table Data */
     
     
     /* Download Listner */
     $('#download').click(function() { ShowDownloadMessage(); });

     function ShowDownloadMessage()
     {
    	 $("#loader").removeClass("hidden");
          window.addEventListener('focus', HideDownloadMessage, false);
     }

     function HideDownloadMessage(){
         window.removeEventListener('focus', HideDownloadMessage, false);                   
         $("#loader").addClass("hidden");
     }     
     /* End Download Listner */
     
     function loader(count){
    	 if(count == 0)	$("#loader").removeClass("hidden");
    	 if(count == 2)	$("#loader").addClass("hidden");
     }
     
     $("#download_all").click(function(){
    	 try{
    		 var varDataTable = $('#userTable').DataTable();
             var data = varDataTable.table().data();
             var order = varDataTable.order();
             var orderColumnIndex = order[0][0],orderSortType = order[0][1];
             var columns_orders = [
            		 'orderDate','poNumber','channelName','orderStatus','sku','productName','quantity','orderTotal','supplierCostPerSku',
            		 'salesPrice','amazonAmount','extNetUnit','profit','roi'
            		 ];
             
             var columns_type = {
    				   'orderDate':'date','poNumber':'int','channelName':'string','orderStatus':'string','sku':'int',
    				   'productName':'string','quantity':'int','orderTotal':'int','supplierCostPerSku':'int',
    				   'salesPrice':'int','amazonAmount':'int','extNetUnit':'int','profit':'int','roi':'int'
    	 			 };
             $.merge( data, $('#userSecoundTable').DataTable().table().data() );
             
             var fieldName = columns_orders[orderColumnIndex];
             var columnType = columns_type[columns_orders[orderColumnIndex]];
             
             if(columnType == 'int'){
        	 	  if(orderSortType == 'desc'){
        	 		 data.sort(function(a, b) {return b[fieldName] - a[fieldName]});
        	   	  }else{
        	   		data.sort(function(a, b) {return a[fieldName] - b[fieldName]});
        	   	  }        	 
             }else if(columnType == 'date'){
            	 data.sort(function(a, b){
            		    var aa = a.orderDate.split('/').reverse().join(),
            		        bb = b.orderDate.split('/').reverse().join();
            		        if(orderSortType == "asc"){
            		            return aa < bb ? -1 : (aa > bb ? 1 : 0);//asc
            		        }else{
            		          return bb < aa ? -1 : (bb > aa ? 1 : 0);//desc
            		        }
            		});
             }
             else{
            	 data.sort(sortByColumn(fieldName, orderSortType));
             }
             reOrderFields(data,columns_orders)
    	 }catch (e) {
			// TODO: handle exception
    		 alert(e.message);
		}
    	 
         
     })
     
     
     function reOrderFields(data,columns_orders){
    	 
    	 downloadJsonData = [];
    	 
    	 for (i = 0; i < data.length; i++) {
    		 downloadJsonData[i] = JSON.parse(JSON.stringify( data[i], columns_orders));
    		}
    	 JSONToCSVConvertor_v1(downloadJsonData,'Complete_Data',true);
     }
     
     /**
      * Function to sort alphabetically an array of objects by some specific key.
      * https://www.sitepoint.com/sort-an-array-of-objects-in-javascript/
      * @param {String} property Key of the object to sort.
      * @param {String} property type of sort.
      */
     
     function sortByColumn(key, order = 'asc') {
    	  return function innerSort(a, b) {
    	    if (!a.hasOwnProperty(key) || !b.hasOwnProperty(key)) {
    	      // property doesn't exist on either object
    	      return 0;
    	    }

    	    const varA = (typeof a[key] === 'string')
    	      ? a[key].toUpperCase() : a[key];
    	    const varB = (typeof b[key] === 'string')
    	      ? b[key].toUpperCase() : b[key];

    	    let comparison = 0;
    	    if (varA > varB) {
    	      comparison = 1;
    	    } else if (varA < varB) {
    	      comparison = -1;
    	    }
    	    return (
    	      (order === 'desc') ? (comparison * -1) : comparison
    	    );
    	  };
    	}
     
     
      /* 
      * Convert Json object to CSV with download.
      * @param json data
      * @param title of the download file name
      * @param Boolean value
      */
     function JSONToCSVConvertor(JSONData, ReportTitle, ShowLabel) {
		  //If JSONData is not an object then JSON.parse will parse the JSON string in an Object
		  var arrData = typeof JSONData != 'object' ? JSON.parse(JSONData) : JSONData;
		
		  var CSV = ',,,,';
		  //Set Report title in first row or line
		
		  CSV += ReportTitle + '\r\n\n';
		
		  //This condition will generate the Label/Header
		  if (ShowLabel) {
		    var row = "";
		
		    //This loop will extract the label from 1st index of on array
		    for (var index in arrData[0]) {
		
		      //Now convert each value to string and comma-seprated
		      row += index + ',';
		    }
		
		    row = row.slice(0, -1);
		
		    //append Label row with line break
		    CSV += row + '\r\n';
		  }
		
		  //1st loop is to extract each row
		  for (var i = 0; i < arrData.length; i++) {
		    var row = "";
		
		    //2nd loop will extract each column and convert it in string comma-seprated
		    for (var index in arrData[i]) {
		      row += '"' + arrData[i][index] + '",';
		    }
		
		    row.slice(0, row.length - 1);
		
		    //add a line break after each row
		    CSV += row + '\r\n';
		  }
		
		  if (CSV == '') {
		    alert("Invalid data");
		    return;
		  }
		
		  //console.log(CSV);
		  //Generate a file name
		  var fileName = "Dalrada_ReconcilReport_";
		  //this will remove the blank-spaces from the title and replace it with an underscore
		  fileName += ReportTitle.replace(/ /g, "_");
		
		  //Initialize file format you want csv or xls
		  var uri = 'data:text/csv;charset=utf-8,' + escape(CSV);
		
		  // Now the little tricky part.
		  // you can use either>> window.open(uri);
		  // but this will not work in some browsers
		  // or you will not get the correct file extension    
		
		  //this trick will generate a temp <a /> tag
		  var link = document.createElement("a");
		  link.href = uri;
		
		  //set the visibility hidden so it will not effect on your web-layout
		  link.style = "visibility:hidden";
		  link.download = fileName + ".csv";
		
		  //this part will append the anchor tag and remove it after automatic click
		  document.body.appendChild(link);
		  link.click();
		  document.body.removeChild(link);
	}
     
     function JSONToCSVConvertor_v1(JSONData, ReportTitle, ShowLabel) {     

    	    //If JSONData is not an object then JSON.parse will parse the JSON string in an Object
    	    var arrData = typeof JSONData != 'object' ? JSON.parse(JSONData) : JSONData;
    	    var CSV = '';    
    	    //This condition will generate the Label/Header
    	    if (ShowLabel) {
    	        var row = "";

    	        //This loop will extract the label from 1st index of on array
    	        for (var index in arrData[0]) {
    	            //Now convert each value to string and comma-seprated
    	            row += index + ',';
    	        }
    	        row = row.slice(0, -1);
    	        //append Label row with line break
    	        CSV += row + '\r\n';
    	    }

    	    //1st loop is to extract each row
    	    for (var i = 0; i < arrData.length; i++) {
    	        var row = "";
    	        //2nd loop will extract each column and convert it in string comma-seprated
    	        for (var index in arrData[i]) {
    	            row += '"' + arrData[i][index] + '",';
    	        }
    	        row.slice(0, row.length - 1);
    	        //add a line break after each row
    	        CSV += row + '\r\n';
    	    }

    	    if (CSV == '') {        
    	        alert("Invalid data");
    	        return;
    	    }   

    	    //this trick will generate a temp "a" tag
    	    var link = document.createElement("a");    
    	    link.id="lnkDwnldLnk";

    	    //this part will append the anchor tag and remove it after automatic click
    	    document.body.appendChild(link);

    	    var csv = CSV;  
    	    blob = new Blob([csv], { type: 'text/csv' }); 
    	    var csvUrl = window.webkitURL.createObjectURL(blob);
    	    var filename = "Dalrada_ReconcilReport_"+ReportTitle+".csv";
    	    //var filename = 'UserExport.csv';
    	    $("#lnkDwnldLnk")
    	    .attr({
    	        'download': filename,
    	        'href': csvUrl
    	    }); 

    	    $('#lnkDwnldLnk')[0].click();    
    	    document.body.removeChild(link);
    	}
     
     
     
 } );