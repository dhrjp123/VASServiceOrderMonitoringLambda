import { SERVICETYPES } from './../db-data';
import { ViewDataService } from './viewdata.service';
import { Component } from '@angular/core';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css']
})
export class AppComponent 
{
  title = 'Service Capacity Tracker';
  serviceTypes=SERVICETYPES;    //SERVICETYPES is a collection of all the services in the dropdown. Defined in db-data.ts
  serviceTypeDisplay="";
  columnsInCityView=7;
  columnsInMerchantView=7;
  serviceTypeSelected;
  constructor()
  {

  }

  createTableRowForCityView(entity,tableId)
  {
      var myTable=document.getElementById(tableId) as HTMLTableElement;
      var newRow=myTable.insertRow(-1);
      newRow.className="CityViewTableRow";
      var newCell=newRow.insertCell(0);
      newCell.innerHTML=`<button class="expand" id="`+entity.entityName+`"><img src="https://www.serviceprovidercentral.in/images/plus.png" id="`+entity.entityName+`" width="10"></button>&nbsp;&nbsp;`+entity.entityName;
      newCell=newRow.insertCell(1);
      for(var j=0; j<entity.capacityList.length;j++)
      {
          newCell=newRow.insertCell(j+2);
          newCell.innerHTML=entity.capacityList[j].availableCapacity+"/"+entity.capacityList[j].totalCapacity;
      }
  }

  createEmptyTableRowToShowMerchantView(tableId,divId)      
  {
      var myTable=document.getElementById(tableId) as HTMLTableElement;
      var newRow=myTable.insertRow(-1);
      var newCell=newRow.insertCell(0);
      newCell.colSpan=this.columnsInCityView+2;
      newCell.innerHTML=`<div style="display:none;" id="`+(divId)+`"></div>`;
  }

  addEventListenerbyClassNameToGetMerchantView(classname)
  {
    var collection=document.getElementsByClassName(classname);
    for(var i=0;i<collection.length;i++)
        document.getElementById(collection[i].id).addEventListener(('click'),this.getMerchantView.bind(this));
  }

  getCityView(serviceType)
  {
    this.serviceTypeSelected=serviceType;
    this.serviceTypeDisplay=" : "+serviceType;
    this.createTableSchemaForCityView();
    var responseObject : any;
    responseObject = new ViewDataService().getViewData(serviceType,"");
    for(var i=0; i<responseObject.entityList.length;i++)
    {
      var entity=responseObject.entityList[i];
      this.createTableRowForCityView(entity,"CityViewTable");
      this.createEmptyTableRowToShowMerchantView("CityViewTable",entity.entityName+"expand");
    }
    this.addEventListenerbyClassNameToGetMerchantView("expand");
  }

  createTable(element,tableId,tableClass)
  {
    element.innerHTML=`
    <table id="`+tableId+`" class="`+tableClass+`">
    </table>`;
  }

  createTableHeaderForCityView(tableId)
  {
    var myTable=document.getElementById(tableId) as HTMLTableElement;
    var row=myTable.insertRow(0);
    row.className="CityViewTableHeader";
    var cell1 = row.insertCell(0);
    var cell2 = row.insertCell(1);
    cell1.innerHTML = "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;City Area";
    cell2.innerHTML = "FADD";
    var date=new Date();
    var k=date.getDate();
    for(var i=0;i<this.columnsInCityView;i++)
    {
      date.setDate(k+i);
      var cell=row.insertCell(i+2);
      cell.innerHTML=`A/T(`+(date.getDate())+`/`+(date.getMonth()+1)+`)`;
    }
  }

  createTableSchemaForCityView()
  {
    var element = document.getElementById("text");
    this.createTable(element,"CityViewTable","");
    this.createTableHeaderForCityView("CityViewTable");
  }

  createTableRowForMerchantView(tableId,entity)
  {
    var myTable=document.getElementById(tableId) as HTMLTableElement;
    var newRow=myTable.insertRow(-1);
    newRow.className="MerchantViewTableRow";
    var newCell=newRow.insertCell(0);
    newCell.innerHTML="&nbsp&nbsp&nbsp&nbsp&nbsp"+entity.entityName;
    for(var j=0; j<entity.capacityList.length;j++)
    {
      newCell=newRow.insertCell(j+1);
      newCell.innerHTML=entity.capacityList[j].availableCapacity+"/"+entity.capacityList[j].totalCapacity;
    }
  }

  getMerchantView(event)
  {
    var divId=event.target.id+"expand";
    var element=document.getElementById(divId);
    if(element.style.display === "block")
      element.style.display = "none";
    else
    {
        this.createTableSchemaForMerchantView(divId);
        var tableId=divId+"tbl";
        var responseObject : any;
        responseObject = new ViewDataService().getViewData(this.serviceTypeSelected,event.target.id);
        for(var i=0; i<responseObject.entityList.length;i++)
        {
          var entity=responseObject.entityList[i];
          this.createTableRowForMerchantView(tableId,entity);
        }
        element.style.display="block";
     }
  }

  createTableHeaderForMerchantView(tableId)
  {
    var myTable=document.getElementById(tableId) as HTMLTableElement;
    var row=myTable.insertRow(0);
    row.className="MerchantViewTableHeader";
    var cell1 = row.insertCell(0);
    cell1.innerHTML = "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;Merchants";
    var date=new Date();
    var k=date.getDate();
    for(var i=0;i<this.columnsInMerchantView;i++)
    {
      date.setDate(k+i);
      var cell=row.insertCell(i+1);
      cell.innerHTML=`A/T(`+(date.getDate())+`/`+(date.getMonth()+1)+`)`;
    }
  }

  createTableSchemaForMerchantView(divId)
  {
    var element = document.getElementById(divId);
    this.createTable(element,divId+"tbl","MerchantViewTable");
    var tableId=divId+"tbl";
    this.createTableHeaderForMerchantView(tableId);
  }
}