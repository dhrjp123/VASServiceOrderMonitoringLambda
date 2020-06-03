import { MERCHANTVIEW, CITYVIEW} from './../db-data';
import { Injectable } from '@angular/core';

@Injectable({
  providedIn: 'root'
})
export class ViewDataService {

  constructor() 
  {

  }
  getViewData(serviceType, cityName)
  {
    if(cityName==="")
      return CITYVIEW;          //CITYVIEW is a JSON object stored in db-data
    else
      return MERCHANTVIEW;      // MERCHANTVIEW is a JSON object stored in db-data
  }
}
