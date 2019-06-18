package kozyriatskyi.anton.sutparser

sealed class DepartmentsParser(val value: String,
                               val baseUrl: String,
                               val departmentsUrl: String,
                               val departmentDefinitions: Set<String>) {

    companion object {
        fun getForLanguage(language: Language): DepartmentsParser = language.getParser()
    }

    abstract fun getDepartments(): List<ParsedDepartment>

    internal class UADepartmentsParser : DepartmentsParser(
            value = "ua",
            baseUrl = "http://www.dut.edu.ua",
            departmentsUrl = "http://www.dut.edu.ua/ua/18-kafedri-struktura-universitetu",
            departmentDefinitions = setOf("Викладацький склад", "Науково-педагогічний склад",
                    "Науково-педагогічний персонал", "Викладацький і допоміжний склад")) {

        override fun getDepartments(): List<ParsedDepartment> {
            val document = loadDocument(departmentsUrl)
            val content = document.body().getElementsByClass("content")[1]

            val lists = content.getElementsByClass("sub_pages_full_list_ul col-xs-12  col-sm-12  col-md-6 col-lg-6")

            val departments = content.getElementsByClass("sub_pages_full_list")
                    .mapIndexed { index, element ->
                        val teachersUrl = lists[index * 2].getElementsByTag("li")
                                .find { departmentDefinitions.contains(it.text()) }
                                ?.getElementsByTag("a")
                                ?.first()
                                ?.attr("href") ?: ""

                        val a = element.getElementsByTag("a").first()

                        return@mapIndexed ParsedDepartment(
                                title = a.ownText(),
                                url = a.attr("href"),
                                teachersListUrl = teachersUrl
                        )
                    }

            return departments
        }
    }

    internal class RUDepartmentsParser : DepartmentsParser(
            value = "ru",
            baseUrl = "http://www.dut.edu.ua",
            departmentsUrl = "http://www.dut.edu.ua/ru/18-kafedri-struktura-universitetu",
            departmentDefinitions = setOf("Преподавательский состав", "Научно-педагогический состав",
                    "Научно-педагогический персонал", "Преподавательский и вспомогательный состав")) {

        override fun getDepartments(): List<ParsedDepartment> {
            val document = loadDocument(departmentsUrl)
            val content = document.body().getElementsByClass("content")[1]

            val lists = content.getElementsByClass("sub_pages_full_list_ul col-xs-12  col-sm-12  col-md-6 col-lg-6")

            val departments = content.getElementsByClass("sub_pages_full_list")
                    .mapIndexed { index, element ->
                        val teachersUrl = lists[index * 2].getElementsByTag("li")
                                .find { departmentDefinitions.contains(it.text()) }
                                ?.getElementsByTag("a")
                                ?.first()
                                ?.attr("href") ?: ""

                        val a = element.getElementsByTag("a").first()

                        return@mapIndexed ParsedDepartment(
                                title = a.ownText(),
                                url = a.attr("href"),
                                teachersListUrl = teachersUrl)
                    }

            return departments
        }
    }

    internal class ENDepartmentsParser : DepartmentsParser(
            value = "eng",
            baseUrl = "http://foreign.dut.edu.ua",
            departmentsUrl = "http://foreign.dut.edu.ua/en/pages/18",
            departmentDefinitions = setOf("Teaching staff", "Department staff")) {

        override fun getDepartments(): List<ParsedDepartment> {
            val document = loadDocument(departmentsUrl)
            val content = document.body().getElementsByClass("content_container")[1]

            val lists = content.getElementsByClass("list")
            val departments = content.getElementsByClass("sub_pages_full_list")
                    .mapIndexed { index, element ->
                        val teachersUrl = lists[index].getElementsByTag("li")
                                .find { departmentDefinitions.contains(it.text()) }
                                ?.getElementsByTag("a")
                                ?.first()
                                ?.attr("href") ?: ""

                        val a = element.getElementsByTag("a").first()

                        return@mapIndexed ParsedDepartment(
                                title = a.ownText(),
                                url = a.attr("href"),
                                teachersListUrl = teachersUrl)
                    }

            return departments
        }
    }


    enum class Language {
        UA, RU, EN;

        internal fun getParser(): DepartmentsParser = when (this) {
            UA -> UADepartmentsParser()
            RU -> RUDepartmentsParser()
            EN -> ENDepartmentsParser()
        }
    }
}

