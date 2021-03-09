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
 
  try {
            //初始化插件
            AllTools.initUtil("包名");

            //设置内存范围
            AllTools.memRange = MemRange.CA;

            //范围搜索,拿到搜索到的值地址集
            System.err.println("开始范围搜索.....");
            ArrayList<Long> rangeSearchAddrs = MemorySearch.rangeSearch(12345, DataType.DWORD);
            System.err.println("范围搜索完毕!共: " + rangeSearchAddrs.size() + "个结果");

            System.err.println("开始偏移4匹配结果999...");
            ArrayList<Long> offsetSearchAddrs = MemorySearch.offsetSearch(999, 4, rangeSearchAddrs, DataType.DWORD);
            System.err.println("偏移搜索完成,共: " + offsetSearchAddrs.size() + "个结果!");

            //获取单个地址信息, 这里你传入什么类型数据就用什么类型接收
            int addrInfo = MemorySearch.getAddrInfo(offsetSearchAddrs.get(0), 0, DataType.DWORD);
            System.err.println("第一个数据为: " + addrInfo);

            //获取地址集数据信息
            ArrayList<Integer> addrInfos = MemorySearch.getAddrInfos(rangeSearchAddrs, 0, DataType.DWORD);
            System.err.println("范围搜索数据结果: ");
            for (int i = 0; i < addrInfos.size(); i++) {
                System.err.println(addrInfos.get(i));
            }

            // 开启socket程序写出结果集数据, 返回线程用于结束输出结果, 数据接收方填入相同的端口号来接收数据
            ServerThread serverThread = MemorySearch.putAddrInfos(rangeSearchAddrs, 0, DataType.DWORD, 8088);

            //设置为false表示结束此线程数据输出
            serverThread.setState(false);

            //写内存
            MemoryWrite.addrWrite(54321,0,rangeSearchAddrs,DataType.DWORD);

            //冻结内存
            MemoryWrite.one = true;//将线程状态设置为true
            MemoryWrite.freezeAddrWrite(54321,0,rangeSearchAddrs,DataType.DWORD,300,ThreadNumber.ONE);

            //关闭冻结
            MemoryWrite.one = false;//将对应的线程号设置为false即可关闭


        } catch (Exception e) {
            e.printStackTrace();
        }
    }
