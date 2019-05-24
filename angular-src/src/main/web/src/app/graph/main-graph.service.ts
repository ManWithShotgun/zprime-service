import {Injectable} from '@angular/core';
import {MainGraph} from './graphD3/main-graph';
import {WebsoketService} from "../service/websoket.service";


@Injectable({
  providedIn: 'root'
})
    export class MainGraphService {

    private MAIN_GRAPH_SELECTOR: string = 'div#svg';
    private LOCAL_STORAGE_PREFERENCES_KEY: string = 'preferences';
    private mainGraphInstance: MainGraph;

    constructor(private websoketService: WebsoketService) {
    }

    ngOnDestroy() {
        this.mainGraphInstance.onGraphDestroy();
    }

    public renderMain() {
        this.mainGraphInstance = new MainGraph(this.MAIN_GRAPH_SELECTOR, this.websoketService);
        this.mainGraphInstance.init();
        // render preference
        this.requestPreferenceLines();
    }

    public requestPreferenceLines() {
        // TODO: make get set method for use JSON and local storage
        let preferences = JSON.parse(localStorage.getItem(this.LOCAL_STORAGE_PREFERENCES_KEY));
        if (!preferences) {
            return;
        }
        preferences.forEach(lineKey => {
            let lineInfo = lineKey.split('_');
            this.mainGraphInstance.requestWholeLine(lineInfo[0], lineInfo[1], lineInfo[2]);
        });
    }

    public getLinesInfo(): Array<string> {
        return this.mainGraphInstance.getLinesKsi();
    }

    public requestLine(ksi, events, cycles) {
        let lineKey = ksi + '_' + events + '_' + cycles;
        if (!this.getLinesInfo().includes(lineKey)) {
            this.addPreference(lineKey);
            this.mainGraphInstance.requestWholeLine(ksi, events, cycles);
        }
    }

    public addPreference(lineKey) {
        let preferences = JSON.parse(localStorage.getItem(this.LOCAL_STORAGE_PREFERENCES_KEY));
        if (!preferences) {
            preferences = [];
        }
        preferences.push(lineKey);
        localStorage.setItem(this.LOCAL_STORAGE_PREFERENCES_KEY, JSON.stringify(preferences));
    }

    public removePreference(lineKey) {
        let preferences = JSON.parse(localStorage.getItem(this.LOCAL_STORAGE_PREFERENCES_KEY));
        let index = preferences.indexOf(lineKey);
        preferences.splice(index, 1);
        localStorage.setItem(this.LOCAL_STORAGE_PREFERENCES_KEY, JSON.stringify(preferences));
    }

    public removeLine(lineKey) {
        console.log('Remove: ' + lineKey);
        this.removePreference(lineKey);
        this.mainGraphInstance.removeLine(lineKey);
    }


}
