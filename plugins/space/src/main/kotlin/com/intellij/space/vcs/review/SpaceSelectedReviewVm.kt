// Copyright 2000-2020 JetBrains s.r.o. Use of this source code is governed by the Apache 2.0 license that can be found in the LICENSE file.
package com.intellij.space.vcs.review

import circlet.code.api.CodeReviewWithCount
import circlet.workspaces.Workspace
import com.intellij.space.vcs.SpaceProjectInfo
import runtime.reactive.MutableProperty

internal interface SpaceSelectedReviewVm {
  val workspace: Workspace

  val projectInfo: SpaceProjectInfo

  val selectedReview: MutableProperty<CodeReviewWithCount?>
}