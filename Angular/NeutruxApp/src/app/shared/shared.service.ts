import { Injectable } from "@angular/core";

@Injectable({ 
    providedIn: 'root'
 })
export class SharedService {

    getTime( dateStr:Date ) {
        let returnValue = ''
        let currentDate:Date = new Date()
        let date:Date = new Date( dateStr )
        
        let diffTime = Math.abs( currentDate.getTime() - date.getTime() )
        let secondsCount = Math.ceil( diffTime/1000 ) 
        let minutesCount = Math.ceil( diffTime/(1000*60) )
        let hoursCount = Math.ceil( diffTime/(1000*60*60) )
        let daysCount = Math.ceil( diffTime/(1000*60*60*24) )
        let monthsCount = Math.ceil( diffTime/(1000*60*60*24*30) )
        let yearsCount = Math.ceil( diffTime/(1000*60*60*24*365) )
        if( secondsCount<60 ) {
            returnValue = secondsCount + ' seconds ago'
        } else if( minutesCount<60 ) {
            returnValue = minutesCount + ' minutes ago'
        } else if( hoursCount<24 ) {
            returnValue = hoursCount + ' hours ago'
        } else if( daysCount<30 ) {
            returnValue = daysCount + ' days ago'
        } else if( monthsCount<12 ) {
            returnValue = monthsCount + ' months ago'
        } else {
            returnValue = yearsCount + ' years ago'
        }

        return returnValue
    }

}