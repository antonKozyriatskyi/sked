package kozyriatskyi.anton.sked.screen.about

import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import kozyriatskyi.anton.sked.BuildConfig
import kozyriatskyi.anton.sked.R
import kozyriatskyi.anton.sked.util.UnitCallback

private const val URL_PRIVACY_POLICY =
    "https://firebasestorage.googleapis.com/v0/b/sked-a797c.appspot.com/o/privacy_policy.html?alt=media&token=cf8d3456-a2ff-49c8-a9f6-f879e0e55775"
private const val URL_TG_APP = "tg://resolve?domain=antonKozyriatskyi"
private const val URL_TG_WEB = "http://www.telegram.me/antonKozyriatskyi"

@Composable
fun AboutScreen(
    navigationIcon: ImageVector = Icons.Default.ArrowBack,
    onNavigateUp: UnitCallback
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = stringResource(R.string.about_title)) },
                navigationIcon = {
                    IconButton(onClick = onNavigateUp) {
                        Icon(navigationIcon, null)
                    }
                },
            )
        }
    ) {
        Column {
            Row {
                Text(text = stringResource(R.string.about_version_name, BuildConfig.VERSION_NAME))

                Text(text = stringResource(id = R.string.about_developed_by))
            }

            Row {
                val context = LocalContext.current

                TextButton(onClick = { tryOpenUri(URL_PRIVACY_POLICY, context) }) {
                    Text(text = stringResource(R.string.about_privacy_policy))
                }

                Divider(
                    color = colorResource(id = R.color.dividerColor),
                    modifier = Modifier.fillMaxHeight()
                )

                TextButton(
                    onClick = {
                        if (tryOpenUri(URL_TG_APP, context).not()) {
                            tryOpenUri(URL_TG_WEB, context)
                        }
                    }
                ) {
                    Text(text = "Telegram")
                }
            }

            LazyColumn {
                items(getLibraries()) { library ->
                    Library(library)
                }
            }
        }
    }
}

@Composable
private fun Library(library: Library) {
    Card {
        Column {
            Text(text = library.name)

            Text(
                text = library.license,
                modifier = Modifier
                    .background(colorResource(R.color.licenseBackground))
                    .padding(8.dp)
            )
        }
    }
}

private fun tryOpenUri(uri: String, context: Context): Boolean {
    val intent = Intent(Intent.ACTION_VIEW, Uri.parse(uri))
    val canOpen = intent.resolveActivity(context.packageManager) != null
    if (canOpen) context.startActivity(intent)

    return canOpen
}

private fun getLibraries(): List<Library> {

    return listOf(
        Library(
            "Jsoup", """The MIT License
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
"""
        ),
        Library(
            "RxRelay", """Copyright 2014 Netflix, Inc.
Copyright 2015 Jake Wharton

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

   http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License."""
        ),
        Library(
            "RxAndroid", """Copyright 2015 The RxAndroid authors

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

    http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License."""
        ),
        Library(
            "RxJava", """Copyright (c) 2016-present, RxJava Contributors.

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License."""
        ),
        Library(
            "Dagger 2", """Copyright 2012 The Dagger Authors

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

   http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License."""
        ),
        Library(
            "Moxy", """The MIT License (MIT)

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
SOFTWARE."""
        )
    ).sortedBy(Library::name)
}

private data class Library(val name: String, val license: String)