import {Question} from "../_models/index";

export class Survey {
    name: String;
    description: String;
    email: String;
    questions: Question[];
}