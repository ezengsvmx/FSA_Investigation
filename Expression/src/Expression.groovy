import groovy.json.JsonBuilder
import groovy.json.JsonSlurper
import java.sql.*
import groovy.sql.Sql


def sql = Sql.newInstance('jdbc:sqlite:SVMXLaptopMobile.dll', "org.sqlite.JDBC")

sql.eachRow('select * from SFExpression') {
    def json = new JsonBuilder()
    json.expression { }
    json.content.expression.expression_id = it.expression_id
    json.content.expression.expression = it.expression
    json.content.expression.expression_name = it.expression_name
    json.content.expression.advance_expression = it.advance_expression
    json.content.expression.source_object_name = it.source_object_name
    json.content.expression.error_message = it.error_message
    json.content.expression.rule_type = it.rule_type
    def array = []
    sql.eachRow("select * from SFExpressionComponent where expression_id = ${it.expression_id} order by sequence") { 
        def expressionComponent = new JsonBuilder()
        expressionComponent.component {}
        expressionComponent.content.component.field_type = it.field_type
        expressionComponent.content.component.operator = it.operator
        expressionComponent.content.component.sequence = it.sequence
        expressionComponent.content.component.source_field_name = it.source_field_name
        expressionComponent.content.component.value = it.value
        expressionComponent.content.component.expression_rule = it.expression_rule
        expressionComponent.content.component.expression_type = it.expression_type
        expressionComponent.content.component.parameter_type = it.parameter_type
        expressionComponent.content.component.formula = it.formula
        expressionComponent.content.component.action_type = it.action_type
        array << expressionComponent.content.component
    }
    json.content.expression.component = array
    println json.toPrettyString()
}

