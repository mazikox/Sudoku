import {Component} from '@angular/core';
import {ClientService} from "../../services/client.service";
import {FormControl, FormGroup} from "@angular/forms";
import {MomentDateAdapter, MAT_MOMENT_DATE_ADAPTER_OPTIONS} from "@angular/material-moment-adapter";
import {DateAdapter, MAT_DATE_FORMATS, MAT_DATE_LOCALE} from '@angular/material/core';

import * as _moment from 'moment';
// tslint:disable-next-line:no-duplicate-imports
import {default as _rollupMoment, Moment} from 'moment';
import {MatDatepicker} from "@angular/material/datepicker";

const moment = _rollupMoment || _moment;
export const MY_FORMATS = {
  parse: {
    dateInput: 'MM/YYYY',
  },
  display: {
    dateInput: 'MM/YYYY',
    monthYearLabel: 'MMM YYYY',
    dateA11yLabel: 'LL',
    monthYearA11yLabel: 'MMMM YYYY',
  },
};
@Component({
  selector: 'app-analytics',
  templateUrl: './analytics.component.html',
  styleUrls: ['./analytics.component.scss'],
  providers: [
    // `MomentDateAdapter` can be automatically provided by importing `MomentDateModule` in your
    // application's root module. We provide it at the component level here, due to limitations of
    // our example generation script.
    {
      provide: DateAdapter,
      useClass: MomentDateAdapter,
      deps: [MAT_DATE_LOCALE, MAT_MOMENT_DATE_ADAPTER_OPTIONS]
    },

    {provide: MAT_DATE_FORMATS, useValue: MY_FORMATS},
  ],
})
export class AnalyticsComponent {

  information: any;
  information1stWeek: any;
  information2ndWeek: any;
  information3rdWeek: any;
  information4thWeek: any;
  information5thWeek: any;

  chart: any;
  isButtonVisible = false;

  date = new FormControl(moment());
  firstDayOfMonth: any = moment().startOf('month').valueOf();
  lastDayOfMonth: any = moment().endOf('month').valueOf();

  chosenYearHandler(normalizedYear: Moment) {
    const ctrlValue = this.date.value;
    // @ts-ignore
    ctrlValue.year(normalizedYear.year());

    this.date.setValue(ctrlValue);
  }

  chosenMonthHandler(normalizedMonth: Moment, datepicker: MatDatepicker<Moment>) {
    const ctrlValue = this.date.value;
    // @ts-ignore
    ctrlValue.month(normalizedMonth.month());

    this.firstDayOfMonth = moment(ctrlValue).startOf('month').valueOf();
    this.lastDayOfMonth = moment(ctrlValue).endOf('month').valueOf();


    console.log('this')
    console.log(this.firstDayOfMonth);

    console.log('this2')
    console.log(this.lastDayOfMonth);
    this.date.setValue(ctrlValue);
    datepicker.close();

    this.clearDatapoints();
    this.getInfo();
    this.getInfoOfMonth();
  }


  getChartInstance(chart: object) {
    this.chart = chart;
    this.chart.options = this.transactionOptions;
    this.chart.options.data = this.options["data"];
    this.getInfo();
    this.getInfoOfMonth();
  }

  transactionInstance() {
    this.chart.options = this.transactionOptions;
    this.chart.options.data = this.options["data"];
    this.chart.render();
    this.clearDatapoints();
    this.isButtonVisible = false;
  }

  categoryHandler = (e: any) => {
    this.chart.options = this.categoryOptions;
    this.getOptions(e.dataPoint.name);
    this.chart.options.data = this.options["Category"];

    this.chart.options.title = {text: e.dataPoint.name}
    this.chart.render();
    this.isButtonVisible = true;
  }

  transactionOptions = {
    animationEnabled: true,
    theme: "light2",
    title: {
      text: "Transactions"
    },
    subtitles: [{
      text: "Click on Any Segment to get Details",
      backgroundColor: "#2eacd1",
      fontSize: 16,
      fontColor: "white",
      padding: 5
    }],
    data: []
  };

  categoryOptions = {
    animationEnabled: true,
    theme: "light2",
    axisY: {
      minimum: 0,
      gridThickness: 0,
      lineThickness: 1
    },
    data: []
  };

  options = {
    "data": [{
      type: "pie",
      title: "transaction",
      name: "transaction",
      cursor: "pointer",
      explodeOnClick: false,
      startAngle: -90,
      click: this.categoryHandler,
      indexLabel: "{name}",
      indexLabelPlacement: "inside",
      indexLabelFontColor: "white",
      dataPoints: [
        {name: "", y: 0},
        {name: "", y: 0},
        {name: "", y: 0},
        {name: "", y: 0},
        {name: "", y: 0},
      ]
    }],
    "Category": [{
      color: "#50b432",
      name: "Returning Visitors",
      type: "column",
      dataPoints: [
        {label: "1st", y: 0},
        {label: "2nd", y: 0},
        {label: "3rd", y: 0},
        {label: "4th", y: 0},
        {label: "5hth", y: 0},
      ]
    }]
  }


  constructor(private clientService: ClientService) {
  }

  getInfo() {
    this.clientService.getAnalytics(this.firstDayOfMonth, this.lastDayOfMonth).subscribe(data => {
      this.information = data;
      console.log(this.information)
      for (let i = 0; i <= 4; i++) {
        this.options.data[0].dataPoints[i].name = this.information[i].categoryCode.name;
        this.options.data[0].dataPoints[i].y = this.information[i].sum;
      }
      this.chart.render();
    });
  }

  getInfoOfMonth() {
    let dateFrom = this.firstDayOfMonth;
    let dateTo = this.lastDayOfMonth;

    let divider = Math.floor((dateTo - dateFrom) / 5);

    this.clientService.getAnalytics(dateFrom, dateFrom + divider).subscribe(data => {
      this.information1stWeek = data;
    });
    this.clientService.getAnalytics(dateFrom + divider, dateFrom + divider * 2).subscribe(data => {
      this.information2ndWeek = data;
      console.log(dateFrom + divider)
      console.log(this.information2ndWeek)
    });
    this.clientService.getAnalytics(dateFrom + divider * 2, dateFrom + divider * 3).subscribe(data => {
      this.information3rdWeek = data;
    });
    this.clientService.getAnalytics(dateFrom + divider * 3, dateFrom + divider * 4).subscribe(data => {
      this.information4thWeek = data;
    });
    this.clientService.getAnalytics(dateFrom + divider * 4, dateFrom + divider * 5).subscribe(data => {
      this.information5thWeek = data;
    });
  }

  getOptions(categoryName: string) {
    const informationWeeks = [
      this.information1stWeek,
      this.information2ndWeek,
      this.information3rdWeek,
      this.information4thWeek,
      this.information5thWeek
    ];

    for (let weekIndex = 0; weekIndex < informationWeeks.length; weekIndex++) {
      const week = informationWeeks[weekIndex];

      for (let i = 0; i < week.length; i++) {
        if (week[i].categoryCode.name == categoryName) {
          this.options.Category[0].dataPoints[weekIndex].y = week[i].sum;
          console.log(week[i].sum)
        }
      }
    }
  }

  clearDatapoints(){
    for (let i = 0; i <=4 ; i++) {
      this.options.Category[0].dataPoints[i].y = 0;
    }
  }
}
