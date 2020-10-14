/**
 *
 */
package tech.pardus.rule.flow.manager.actions;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;
import java.util.Objects;
import java.util.Set;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import tech.pardus.rule.flow.manager.SpringContext;
import tech.pardus.rule.flow.manager.annotattions.DispatcherBean;
import tech.pardus.utilities.LambdaWrapper;
import tech.pardus.utilities.ReflectionUtils;

/**
 * @author deniz.toktay
 * @since Sep 26, 2020
 */
@Service
public class ActionDispatcherManager {

	private HashMap<String, ActionBeanDefn> beanList;

	@Autowired
	private List<ActionDispatcher> dispatchers;

	@Value("${search-native-dispatchers:false}")
	private boolean searchForNativeJavaDispatcher;

	public void init() {
		if (MapUtils.isEmpty(beanList)) {
			beanList = new HashMap<>();
			initiate();
		}
	}

	private void initiate() {
		processBean(SpringContext.getActionBean(NullActionDispatcher.class));
		if (CollectionUtils.isNotEmpty(dispatchers)) {
			dispatchers.stream().forEach(this::processBean);
		}
		if (searchForNativeJavaDispatcher) {
			Set<Class<? extends ActionDispatcher>> dispatcherSet = ReflectionUtils
			        .listOfExtendedClasses(ActionDispatcher.class);
			dispatcherSet.stream().filter(t -> !isBeanRegistered(t))
			        .map(LambdaWrapper.functionChecker(t -> (ActionDispatcher) ReflectionUtils.initClass(t)))
			        .forEach(this::processBean);
		}
	}

	private <T extends ActionDispatcher> void processBean(T dispatcher) {
		var annotationProcessor = new AnnotationProcessor(dispatcher.getClass());
		var beandef = new ActionBeanDefn(annotationProcessor.isSpringAnnotationExists(), annotationProcessor.getName(),
		        dispatcher.getClass());
		beanList.put(annotationProcessor.getName(), beandef);
	}

	private boolean isBeanRegistered(Class<? extends ActionDispatcher> clazz) {
		return beanList.entrySet().stream().map(Entry::getValue).filter(t -> clazz.isAssignableFrom(t.getClazz()))
		        .findFirst().map(t -> Boolean.TRUE).orElse(Boolean.FALSE);
	}

	public void runDispatcher(String name, String... args) throws Exception {
		var beanDef = beanList.get(name);
		if (Objects.isNull(beanDef)) {
			beanDef = beanList.get("Default");
		}
		if (beanDef.isSpringBean()) {
			var bean = SpringContext.getActionBean(beanDef.getClazz());
			bean.fire(args);
		} else {
			var bean = (ActionDispatcher) ReflectionUtils.initClass(beanDef.getClazz());
			bean.fire(args);
		}
	}

	/**
	 * @author deniz.toktay
	 * @since Sep 26, 2020
	 */
	@Getter
	@Setter
	@AllArgsConstructor
	@NoArgsConstructor
	public class ActionBeanDefn implements Serializable {

		/**
		*
		*/
		private static final long serialVersionUID = 1L;

		private boolean springBean;

		private String name;

		private Class<? extends ActionDispatcher> clazz;

	}

	/**
	 * @author deniz.toktay
	 * @since Sep 26, 2020
	 */
	@Getter
	@Setter
	class AnnotationProcessor {

		private String name;

		private boolean springAnnotationExists;

		public AnnotationProcessor(Class<? extends ActionDispatcher> clazz) {
			this.name = clazz.getSimpleName();
			this.springAnnotationExists = false;
			for (var annot : clazz.getAnnotations()) {
				if (StringUtils.equals(annot.annotationType().getSimpleName(), "DispatcherBean")) {
					var beanAnnotation = clazz.getAnnotation(DispatcherBean.class);
					this.name = beanAnnotation.name();
				} else if (StringUtils.equals(annot.annotationType().getSimpleName(), "Component")
				        || StringUtils.equals(annot.annotationType().getSimpleName(), "Service")) {
					this.springAnnotationExists = true;
				}
			}
		}

	}

}