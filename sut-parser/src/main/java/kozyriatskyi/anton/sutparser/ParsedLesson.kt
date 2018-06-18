package kozyriatskyi.anton.sutparser

data class ParsedLesson(val date: String,
                        val number: String,
                        val type: String,
                        val cabinet: String,
                        val shortName: String,
                        val name: String,
                        val addedOnDate: String,
                        val addedOnTime: String,
                        val who: String,
                        val whoShort: String)