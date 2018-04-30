package com.andy.gomoku.conf;

import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

import com.andy.gomoku.game.Global;
import com.andy.gomoku.game.task.TimerManager;
import com.andy.gomoku.utils.GameConf;

@Component
public class StartupListener implements ApplicationListener<ApplicationReadyEvent> {

    public void onApplicationEvent(ApplicationReadyEvent event) {
    	
    	// 初始化配置
    	GameConf.init();
    	initRank(event);
    	
    	TimerManager.init();
    }

	private void initRank(ApplicationReadyEvent event) {
		Global.refreshRanks();
	}
    
//	private void initRank(ApplicationReadyEvent event) {
//    	Map<String, FlowAdapter> flowAdapters = event.getApplicationContext().getBeansOfType(FlowAdapter.class);
//    	if(flowAdapters != null){
//    		for(FlowAdapter adapter:flowAdapters.values()){
//    			AdapterManager.instance().addFlowAdapter(adapter);
//    		}
//    	}
//	}


}
