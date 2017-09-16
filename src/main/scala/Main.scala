import java.util.Optional

import org.ow2.authzforce.core.pdp.api.AttributeFQNs
import org.ow2.authzforce.core.pdp.api.value.{Bags, StandardDatatypes, StringValue}
import org.ow2.authzforce.core.pdp.impl.BasePdpEngine
import org.ow2.authzforce.xacml.identifiers.{XACMLAttributeCategory, XACMLAttributeId}
import org.slf4j.LoggerFactory

/**
  * Created by rudolf on 2017/09/16.
  */
object Main extends App {
  val log = LoggerFactory.getLogger("pdp")
  val url = getClass.getResource("/pdp-config.xml")
  val pdp = BasePdpEngine.getInstance(url.getFile)

  val requestBuilder = pdp.newRequestBuilder(3, 3)

  //Subject
  val subjectIdAttributeId = AttributeFQNs.newInstance(XACMLAttributeCategory.XACML_1_0_ACCESS_SUBJECT.value(),
    Optional.empty(), XACMLAttributeId.XACML_1_0_SUBJECT_ID.value())
  val subjectIdAttributeValues = Bags.singletonAttributeBag(StandardDatatypes.STRING_FACTORY.getDatatype, new StringValue("Julius Hibbert"))
  requestBuilder.putNamedAttributeIfAbsent(subjectIdAttributeId, subjectIdAttributeValues)

  //Resource
  val resourceIdAttributeId = AttributeFQNs.newInstance(XACMLAttributeCategory.XACML_3_0_RESOURCE.value(), Optional.empty(), XACMLAttributeId.XACML_1_0_RESOURCE_ID.value())
  val resourceIdAttributeValues = Bags.singletonAttributeBag(StandardDatatypes.STRING_FACTORY.getDatatype, new StringValue("http://medico.com/record/patient/BartSimpson"))
  requestBuilder.putNamedAttributeIfAbsent(resourceIdAttributeId, resourceIdAttributeValues)

  //Action
  val actionIdAttributeId = AttributeFQNs.newInstance(XACMLAttributeCategory. XACML_3_0_ACTION.value(), Optional.empty(), XACMLAttributeId.XACML_1_0_ACTION_ID.value())
  val actionIdAttributeValues = Bags.singletonAttributeBag(StandardDatatypes.STRING_FACTORY.getDatatype, new StringValue("read"))
  requestBuilder.putNamedAttributeIfAbsent(actionIdAttributeId, actionIdAttributeValues)

  val result = pdp.evaluate(requestBuilder.build(true))
  log.debug(result.getDecision.value)
}
