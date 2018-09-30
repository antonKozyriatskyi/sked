package kozyriatskyi.anton.sked.about

import android.content.ActivityNotFoundException
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.TextView
import kozyriatskyi.anton.sked.BuildConfig
import kozyriatskyi.anton.sked.data.pojo.Library
import java.util.*


class AboutActivity : AppCompatActivity(), View.OnClickListener {

    companion object {
        private const val URL_PRIVACY_POLICY = "https://firebasestorage.googleapis.com/v0/b/sked-a797c.appspot.com/o/privacy_policy.html?alt=media&token=cf8d3456-a2ff-49c8-a9f6-f879e0e55775"
        private const val URL_VK = "http://vk.com/kozyriatskyi"
        private const val URL_TG_APP = "tg://resolve?domain=antonKozyriatskyi"
        private const val URL_TG_WEB = "http://www.telegram.me/antonKozyriatskyi"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_about)

        val recycler = findViewById<RecyclerView>(R.id.about_rv_libraries)
        recycler.adapter = LibrariesAdapter(getLibraries())
        recycler.layoutManager = LinearLayoutManager(this)

        findViewById<TextView>(R.id.about_tv_version).text = getString(R.string.about_version_name,
                BuildConfig.VERSION_NAME)
        findViewById<TextView>(R.id.about_tv_telegram).setOnClickListener(this)
        findViewById<TextView>(R.id.about_tv_vk).setOnClickListener(this)
        findViewById<TextView>(R.id.about_tv_privacy_policy).setOnClickListener(this)
    }


    override fun onClick(view: View) {
        when (view.id) {
            R.id.about_tv_telegram -> {
                val intent = Intent(Intent.ACTION_VIEW, Uri.parse(URL_TG_APP))

                try {
                    startActivity(intent)
                } catch (e: ActivityNotFoundException) {
                    tryOpenBrowser(URL_TG_WEB)
                }

            }
            R.id.about_tv_vk -> tryOpenBrowser(URL_VK)
            R.id.about_tv_privacy_policy -> tryOpenBrowser(URL_PRIVACY_POLICY)
        }
    }

    private fun tryOpenBrowser(url: String) {
        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
        if (intent.resolveActivity(packageManager) != null) {
            startActivity(intent)
        }
    }

    private fun getLibraries(): ArrayList<Library> {

        val libs = ArrayList<Library>()

        libs.add(Library("Jsoup", """The MIT License
Copyright © 2009 - 2017 Jonathan Hedley (jonathan@hedley.net)
Permission is hereby granted, free of charge, to any person obtaining a copy of this software and
 associated documentation files (the "Software"), to deal in the Software without restriction,
  including without limitation the rights to use, copy, modify, merge, publish, distribute,
   sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is
   furnished to do so, subject to the following conditions:
The above copyright notice and this permission notice shall be included in all copies or substantial
 portions of the Software.
THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT
 NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND
 NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM,
  DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
   OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
"""))

        /*libs.add(Library("Android-Job", """Copyright (c) 2007-2017 by Evernote Corporation, All rights reserved.

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

    http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License."""))*/

        libs.add(Library("RxRelay", """Copyright 2014 Netflix, Inc.
Copyright 2015 Jake Wharton

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

   http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License."""))

        libs.add(Library("RxAndroid", """Copyright 2015 The RxAndroid authors

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

    http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License."""))

        libs.add(Library("RxJava", """Copyright (c) 2016-present, RxJava Contributors.

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License."""))

        libs.add(Library("Dagger 2", """Copyright 2012 The Dagger Authors

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

   http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License."""))

        libs.add(Library("Moxy", """The MIT License (MIT)

Copyright (c) 2016 Arello Mobile

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all
copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
SOFTWARE."""))

        libs.sortBy { it.name }

        return libs
    }
}

