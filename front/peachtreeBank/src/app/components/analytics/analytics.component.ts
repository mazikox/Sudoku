import {Component} from '@angular/core';
import {ClientService} from "../../services/client.service";
import {FormControl} from "@angular/forms";
import {MAT_MOMENT_DATE_ADAPTER_OPTIONS, MomentDateAdapter} from "@angular/material-moment-adapter";
import {DateAdapter, MAT_DATE_FORMATS, MAT_DATE_LOCALE} from '@angular/material/core';
// tslint:disable-next-line:no-duplicate-imports
import * as _moment from 'moment';
import {default as _rollupMoment, Moment} from 'moment';
import {MatDatepicker} from "@angular/material/datepicker";
import {catchError, forkJoin, retry, Subject, takeUntil, throwError} from "rxjs";

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

  statistics: any;
  statistics1stWeek: any;
  statistics2ndWeek: any;
  statistics3rdWeek: any;
  statistics4thWeek: any;
  statistics5thWeek: any;

  chart: any;
  isButtonVisible = false;
  loading: boolean = true;

  date = new FormControl(moment());
  firstDayOfMonth: any = moment().startOf('month').valueOf();
  lastDayOfMonth: any = moment().endOf('month').valueOf();
  destroy = new Subject<void>();

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

    this.loading = true;
    this.clearDataPoints();
    this.clearStatistics();
    this.getInfoOfMonth();
    this.getInfoOfMonthWeekly();
  }


  getChartInstance(chart: object) {
    this.chart = chart;
    this.chart.options = this.transactionOptions;
    this.chart.options.data = this.options["data"];
    this.getInfoOfMonth();
    this.getInfoOfMonthWeekly();
  }

  transactionInstance() {
    this.chart.options = this.transactionOptions;
    this.chart.options.data = this.options["data"];
    this.chart.render();
    this.isButtonVisible = false;
    this.clearDataPoints();
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
    data: [],
    backgroundColor: [
      "#E9F1FA"
    ]
  };

  categoryOptions = {
    animationEnabled: true,
    theme: "light2",
    axisY: {
      minimum: 0,
      gridThickness: 0,
      lineThickness: 1
    },
    data: [],
    backgroundColor: [
      "#E9F1FA"
    ]
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

  getInfoOfMonth() {
    this.clientService.getAnalytics(this.firstDayOfMonth, this.lastDayOfMonth).pipe()
      .subscribe(data => {
        this.statistics = data;
        console.log(this.statistics)
        for (let i = 0; i <= 4; i++) {
          this.options.data[0].dataPoints[i].name = this.statistics[i].categoryCode.name;
          this.options.data[0].dataPoints[i].y = this.statistics[i].sum;
        }
        this.loading = false;
        this.chart.render();
      });
  }

  getInfoOfMonthWeekly() {
    let dateFrom = this.firstDayOfMonth;
    let dateTo = this.lastDayOfMonth;

    let divider = Math.floor((dateTo - dateFrom) / 5);
    const requests = [];


    for (let i = 0; i < 5; i++) {
      const fromDate = dateFrom + divider * i;
      const toDate = dateFrom + divider * (i + 1);

      requests.push(
        this.clientService.getAnalytics(fromDate, toDate).pipe(
          retry(3),
          takeUntil(this.destroy)
        )
      );
    }

    forkJoin(requests).subscribe(dataArray => {
      this.statistics1stWeek = dataArray[0];
      this.statistics2ndWeek = dataArray[1];
      this.statistics3rdWeek = dataArray[2];
      this.statistics4thWeek = dataArray[3];
      this.statistics5thWeek = dataArray[4];
    });
  }

  getOptions(categoryName: string) {
    const informationWeeks = [
      this.statistics1stWeek,
      this.statistics2ndWeek,
      this.statistics3rdWeek,
      this.statistics4thWeek,
      this.statistics5thWeek
    ];

    for (let weekIndex = 0; weekIndex < informationWeeks.length; weekIndex++) {
      const week = informationWeeks[weekIndex];

      console.log(week)
      for (const element of week) {
        if (element.categoryCode.name == categoryName) {
          this.options.Category[0].dataPoints[weekIndex].y = element.sum;
        }
      }
    }
  }

  clearDataPoints() {
    for (let i = 0; i <= 4; i++) {
      this.options.Category[0].dataPoints[i].y = 0;
    }
  }

  clearStatistics(){
    this.statistics1stWeek = [];
    this.statistics2ndWeek = [];
    this.statistics3rdWeek = [];
    this.statistics4thWeek = [];
    this.statistics5thWeek = [];
  }
}
