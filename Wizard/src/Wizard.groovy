import groovy.json.JsonBuilder
import groovy.json.JsonSlurper
import groovy.sql.Sql


def sql = Sql.newInstance('jdbc:sqlite:SVMXLaptopMobile.dll', "org.sqlite.JDBC")

sql.eachRow('select * from SFWizard') {
    def json = new JsonBuilder()
    json.wizard { }
    json.content.wizard.wizard_id = it.wizard_id
    json.content.wizard.object_name = it.object_name
    json.content.wizard.expression_id = it.expression_id
    json.content.wizard.wizard_description = it.wizard_description
    json.content.wizard.wizard_name = it.wizard_name
    json.content.wizard.sequence = it.sequence
    json.content.wizard.is_active = it.is_active
    def array = []
    sql.eachRow("select * from SFWizardComponent where wizard_id = ${it.wizard_id} order by sequence") {
        def wizardComponent = new JsonBuilder()
        wizardComponent.component {}
        wizardComponent.content.component.action_id = it.action_id
        wizardComponent.content.component.action_description = it.action_description
        wizardComponent.content.component.expression_id = it.expression_id
        wizardComponent.content.component.process_id = it.process_id
        wizardComponent.content.component.action_type = it.action_type
        wizardComponent.content.component.action_name = it.action_name
        wizardComponent.content.component.perform_sync = it.perform_sync
        wizardComponent.content.component.class_name = it.class_name
        wizardComponent.content.component.method_name = it.method_name
        wizardComponent.content.component.sequence = it.sequence
        wizardComponent.content.component.customActionType = it.customActionType
        wizardComponent.content.component.customUrl = it.customUrl
        def getPageData = "select * from SFPageLayout where page_layout_id = (select page_layout_id from SFProcess where process_id = '" + it.process_id + "')"
        //println getPageData
        sql.eachRow(getPageData) {
            def slurper = new JsonSlurper()
            wizardComponent.content.component.page_data = slurper.parseText(it.page_data)
        }

        array << wizardComponent.content.component
    }
    json.content.wizard.component = array
    
    println json.toPrettyString()
}
