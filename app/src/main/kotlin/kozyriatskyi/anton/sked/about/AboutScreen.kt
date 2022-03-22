package kozyriatskyi.anton.sked.about

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.util.TypedValue
import androidx.annotation.AttrRes
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import kozyriatskyi.anton.sked.BuildConfig
import kozyriatskyi.anton.sked.R
import kozyriatskyi.anton.sked.data.pojo.Library
import kozyriatskyi.anton.sked.ui.SecondaryText
import kozyriatskyi.anton.sked.util.Callback


/**
 * Created by Backbase R&D B.V. on 21.03.2022.
 */

private const val URL_PRIVACY_POLICY =
    "https://firebasestorage.googleapis.com/v0/b/sked-a797c.appspot.com/o/privacy_policy.html?alt=media&token=cf8d3456-a2ff-49c8-a9f6-f879e0e55775"
private const val URL_TG_APP = "tg://resolve?domain=antonKozyriatskyi"
private const val URL_TG_WEB = "http://www.telegram.me/antonKozyriatskyi"

@Composable
fun AboutScreen(
    onNavigateUp: Callback
) {
    val context = LocalContext.current

    Scaffold(
        topBar = {
            TopAppBar(
                backgroundColor = MaterialTheme.colors.primary,
                title = {
                    Text(text = stringResource(id = R.string.about_activity_title))
                },
                navigationIcon = {
                    IconButton(onClick = onNavigateUp) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = null
                        )
                    }
                },
            )
        }
    ) {
        Column(
            modifier = Modifier.fillMaxSize()
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
                    .padding(top = 8.dp)
            ) {
                Text(
                    text = stringResource(R.string.about_version_name, BuildConfig.VERSION_NAME),
                    fontWeight = FontWeight.Bold,
                    style = MaterialTheme.typography.body1
                )

                Spacer(modifier = Modifier.width(8.dp))

                SecondaryText(
                    text = stringResource(R.string.about_developed_by),
                    style = MaterialTheme.typography.body1
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            Row(
                modifier = Modifier
                    .height(IntrinsicSize.Min)
                    .padding(horizontal = 8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                TextButton(onClick = {
                    context.tryOpenUri(URL_PRIVACY_POLICY)
                }) {
                    Text(
                        text = stringResource(id = R.string.about_privacy_policy),
                        color = colorResource(id = R.color.redAс100)
                    )
                }

                Box(
                    modifier = Modifier
                        .padding(8.dp)
                ) {
                    Spacer(
                        modifier = Modifier
                            .fillMaxHeight(.8f)
                            .width(1.dp)
                            .background(Color(colorAttribute(R.attr.dividerColor)))

                    )
                }

                TextButton(
                    onClick = {
                        if (context.tryOpenUri(URL_TG_APP).not()) {
                            context.tryOpenUri(URL_TG_WEB)
                        }
                    }
                ) {
                    Text(
                        text = "Telegram",
                        color = colorResource(id = R.color.blue300)
                    )
                }
            }

            LazyColumn(
                contentPadding = PaddingValues(all = 8.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                items(getLibraries()) { library ->
                    LibraryCard(library)
                }
            }
        }

    }
}

@Composable
fun LibraryCard(library: Library) {
    Card {
        Column(
            modifier = Modifier.padding(all = 8.dp)
        ) {
            Text(
                text = library.name,
                fontWeight = FontWeight.Bold
            )

            Spacer(
                modifier = Modifier.height(8.dp)
            )

            Text(
                text = library.license,
                style = MaterialTheme.typography.body2,
                modifier = Modifier
                    .background(Color(colorAttribute(attr = R.attr.dividerColor)))
                    .padding(all = 8.dp)
            )
        }
    }
}

private fun Context.tryOpenUri(uri: String): Boolean {
    val intent = Intent(Intent.ACTION_VIEW, Uri.parse(uri))
    val canOpen = intent.resolveActivity(packageManager) != null
    if (canOpen) {
        startActivity(intent)
    }

    return canOpen
}

@Composable
private fun colorAttribute(@AttrRes attr: Int): Int {
    val theme = LocalContext.current.theme
    val typedValue = TypedValue()
    theme.resolveAttribute(attr, typedValue, true)
    return typedValue.data
}

private fun getLibraries(): List<Library> {

    val libs = ArrayList<Library>()

    libs.add(
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
        )
    )

    libs.add(
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
        )
    )

    libs.add(
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
        )
    )

    libs.add(
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
        )
    )

    libs.add(
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
        )
    )

    libs.add(
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
    )

    libs.sortBy { it.name }

    return libs
}
