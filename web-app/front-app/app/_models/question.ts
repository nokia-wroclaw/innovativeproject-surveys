export class Question {
	constructor(
		public id: number,
		public question: String,
		public questionType: String,
		public possibleAnswers: String[]
	) {}
}