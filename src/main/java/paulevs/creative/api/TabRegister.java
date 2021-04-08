package paulevs.creative.api;

import net.modificationstation.stationloader.api.common.event.Event;
import net.modificationstation.stationloader.api.common.factory.EventFactory;

public interface TabRegister {
	Event<TabRegister> EVENT = EventFactory.INSTANCE.newEvent(TabRegister.class, (listeners) -> {
		return () -> {
			TabRegister[] var1 = listeners;
			int var2 = listeners.length;

			for (int var3 = 0; var3 < var2; ++var3) {
				TabRegister event = var1[var3];
				event.registerTabs();
			}

		};
	});

	void registerTabs();
}
