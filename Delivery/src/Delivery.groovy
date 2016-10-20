import groovy.json.JsonBuilder
import groovy.json.JsonSlurper
import groovy.sql.Sql


def sql = Sql.newInstance('jdbc:sqlite:SVMXLaptopMobile.dll', "org.sqlite.JDBC")

sql.eachRow('select * from SFProcess') {
    def json = new JsonBuilder()
    json.delivery { }
    json.content.delivery.process_id = it.process_id
    json.content.delivery.process_unique_id = it.process_unique_id
    json.content.delivery.process_type = it.process_type
    json.content.delivery.process_name = it.process_name
    json.content.delivery.process_description = it.process_description
    json.content.delivery.page_layout_id = it.page_layout_id
    json.content.delivery.process_info = it.process_info
    json.content.delivery.doc_template_id = it.doc_template_id
    sql.eachRow("select * from SFPageLayout where page_layout_id = ${it.page_layout_id}") {
        def slurper = new JsonSlurper()
        json.content.delivery.page_data = slurper.parseText(it.page_data)
    }
    
    println json.toPrettyString()
}
