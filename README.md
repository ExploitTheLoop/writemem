# writemem

Add it in your root build.gradle at the end of repositories:

	allprojects {
		repositories {
			...
			maven { url 'https://jitpack.io' }
		}
	}
  
  Step 2. Add the dependency

	dependencies {
	        implementation 'com.github.ExploitTheLoop:writemem:Tag'
	}
	
 step 3 example 
 

            
            AllTools.initUtil("包名");

            
            AllTools.memRange = MemRange.CA;

           
            System.err.println("开始范围搜索.....");
            ArrayList<Long> rangeSearchAddrs = MemorySearch.rangeSearch(12345, DataType.DWORD);
            System.err.println("范围搜索完毕!共: " + rangeSearchAddrs.size() + "个结果");

            System.err.println("开始偏移4匹配结果999...");
            ArrayList<Long> offsetSearchAddrs = MemorySearch.offsetSearch(999, 4, rangeSearchAddrs, DataType.DWORD);
            System.err.println("偏移搜索完成,共: " + offsetSearchAddrs.size() + "个结果!");
            int addrInfo = MemorySearch.getAddrInfo(offsetSearchAddrs.get(0), 0, DataType.DWORD);
            System.err.println("第一个数据为: " + addrInfo);

            
            ArrayList<Integer> addrInfos = MemorySearch.getAddrInfos(rangeSearchAddrs, 0, DataType.DWORD);
            System.err.println("范围搜索数据结果: ");
            for (int i = 0; i < addrInfos.size(); i++) {
                System.err.println(addrInfos.get(i));
            }

            
            ServerThread serverThread = MemorySearch.putAddrInfos(rangeSearchAddrs, 0, DataType.DWORD, 8088);

            
            serverThread.setState(false);

            
            MemoryWrite.addrWrite(54321,0,rangeSearchAddrs,DataType.DWORD);

            
            MemoryWrite.one = true;//将线程状态设置为true
            MemoryWrite.freezeAddrWrite(54321,0,rangeSearchAddrs,DataType.DWORD,300,ThreadNumber.ONE);

           
            MemoryWrite.one = false;//将对应的线程号设置为false即可关闭


       
